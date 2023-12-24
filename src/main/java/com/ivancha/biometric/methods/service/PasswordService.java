package com.ivancha.biometric.methods.service;

import com.ivancha.biometric.methods.dao.PasswordDao;
import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.StatisticCreateDto;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class PasswordService {

    private final PasswordDao passwordDao;


    public long save(PasswordCreateDto passwordCreateDto) {

        try {
            return passwordDao.create(passwordCreateDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<Integer, Integer>> findAllTimeBetweenPressesFor(long passwordId) {
        try {
            return passwordDao.findAllTimeBetweenPressesFor(passwordId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Map<Integer, Integer>> findAllKeyPressTimeFor(long passwordId) {
        try {
            return passwordDao.findAllKeyPressTimeFor(passwordId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void save(StatisticCreateDto statisticCreateDto) {
        try {
            passwordDao.create(statisticCreateDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
