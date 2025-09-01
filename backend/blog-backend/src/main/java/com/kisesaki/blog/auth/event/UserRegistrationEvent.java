package com.kisesaki.blog.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserRegistrationEvent {
    private final Long userId;
    private final String username;
    private final String email;
    private final LocalDateTime registrationTime;
}