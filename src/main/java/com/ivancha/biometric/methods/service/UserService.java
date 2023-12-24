package com.ivancha.biometric.methods.service;

import com.ivancha.biometric.methods.dao.UserDao;
import com.ivancha.biometric.methods.dto.UserCreateDto;
import com.ivancha.biometric.methods.dto.UserReadDto;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public Long create(UserCreateDto userDto) {

        try {
            return userDao.create(userDto.nickname());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserReadDto> findAll() {

        try {
            return userDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
