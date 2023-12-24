package com.ivancha.biometric.methods.dto;

import java.util.Map;

// TODO: 16.12.2023 изменить на получение эталонов
public record PasswordReadDto(
        Long id,
        String value,
        Map<Integer, Integer> timeBetweenPresses,
        Map<Integer, Integer> keyPressTime
)
{ }
