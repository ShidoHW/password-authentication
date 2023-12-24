package com.ivancha.biometric.methods.dto;

public record UserReadDto(Long id,
                          String nickname,
                          PasswordReadDto passwordReadDto)
{ }
