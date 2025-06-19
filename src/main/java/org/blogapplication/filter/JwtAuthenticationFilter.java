
package org.blogapplication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.blogapplication.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/public");
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = null;

        // 1. Try to get JWT from Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        // 2. If not found, try to get JWT from cookies
        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 3. Validate and set authentication
        if (jwt != null) { // Check if the JWT toke is not null
            try {
                String email = jwtUtil.extractUsername(jwt); // get email from the jwt token
                String roleString = jwtUtil.extractRoles(jwt); // get roles from the jwt token

                //run only if email not null and provide email not authenticated
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // load the user details from the database using email
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // validate the token
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        // convert the coma separated roles string into a list of spring security authority
                        List<SimpleGrantedAuthority> authorities = roleString.isBlank() ? Collections.emptyList() : Arrays.stream(roleString.split(",")).map(String::trim).map(SimpleGrantedAuthority::new).toList();

                        // created an authentication token using the user details and extract authorities
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                        // set additional details from the HTTP request [session,etc...]
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // set the authentication object into security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                // any exception response set unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
