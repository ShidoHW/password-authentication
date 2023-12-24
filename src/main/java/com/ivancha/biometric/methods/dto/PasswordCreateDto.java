package com.ivancha.biometric.methods.dto;

import java.util.Map;


public record PasswordCreateDto(
        String value,
        Long userId,
        Map<Integer, Integer> timeBetweenPresses,
        Map<Integer, Integer> keyPressTime
) {}
