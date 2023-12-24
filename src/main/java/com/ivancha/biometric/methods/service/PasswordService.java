package com.ivancha.biometric.methods.service;

import com.ivancha.biometric.methods.dao.PasswordDao;
import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;


@RequiredArgsConstructor
public class PasswordService {

    private final PasswordDao passwordDao;

    public Long create(PasswordCreateDto passwordCreateDto) {

        try {
            return passwordDao.create(passwordCreateDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
