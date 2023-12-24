package com.ivancha.biometric.methods.dto;

import java.util.Map;

public record StatisticCreateDto(
        long passwordId,
        Map<Integer, Integer> timeBetweenPresses,
        Map<Integer, Integer> keyPressTime
){
}
