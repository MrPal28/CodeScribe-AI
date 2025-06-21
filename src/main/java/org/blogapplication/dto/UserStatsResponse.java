package org.blogapplication.dto;

import org.blogapplication.constants.UserStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStatsResponse {
  private UserStatus status;
}
