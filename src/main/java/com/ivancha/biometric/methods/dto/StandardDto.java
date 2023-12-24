package com.ivancha.biometric.methods.dto;

public record StandardDto(
        int min,
        int max) {

    @Override
    public String toString() {

        return "[" + min + ", " + max + "]";
    }
}
