package com.ivancha.biometric.methods.dto;

import java.util.List;

public record AuthenticationDto(
        String nickname,
        String password,
        List<Integer> timeBetweenPresses,
        List<Integer> keyPressTime
)
{ }
