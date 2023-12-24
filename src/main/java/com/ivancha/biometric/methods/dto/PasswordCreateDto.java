package com.ivancha.biometric.methods.dto;

import java.util.Map;


public record PasswordCreateDto(
        String value,
        Long userId,
        Map<Integer, StandardDto> tbpStandard,
        Map<Integer, StandardDto> kpsStandard
) {}
