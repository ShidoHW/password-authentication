package com.ivancha.biometric.methods.dto;

import java.util.Map;


public record PasswordReadDto(
        Long id,
        String value,
        Map<Integer, StandardDto> standardTbp,
        Map<Integer, StandardDto> standardKpt
)
{ }
