package com.ivancha.biometric.methods.dto;

import java.util.Map;

public record PasswordReadDto(
        Long id,
        String value,
        Map<Integer, Integer> timeBetweenPresses,
        Map<Integer, Integer> keyPressTime
)
{ }
