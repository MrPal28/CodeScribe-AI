package org.blogapplication.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtilityService {

    public String getLoggedInUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
