package com.ivancha.biometric.methods.service;

import com.ivancha.biometric.methods.Utils;
import com.ivancha.biometric.methods.dao.UserDao;
import com.ivancha.biometric.methods.dto.*;
import com.ivancha.biometric.methods.gui.window.AuthenticationPanel;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public class UserService {

    private static final double STUDENT_COEFFICIENT = 3.07;

    private final UserDao userDao;
    private final PasswordService passwordService;

    public boolean isInfCorrectForUser(AuthenticationDto authenticationDto)
    {
        UserReadDto userDto = findByNickname(authenticationDto.nickname())
                .orElseThrow(() -> new RuntimeException("Не удалось найти запись с таким username: " + authenticationDto.nickname()));

        PasswordReadDto passwordDto = userDto.passwordReadDto();

        if(!authenticationDto.password().equals(passwordDto.value()))
            return false;

        Map<Integer, StandardDto> tbpStandard = passwordDto.standardTbp();
        Map<Integer, StandardDto> kptStandard = passwordDto.standardKpt();

        List<Integer> tbpHammingVec = Utils.calcHammingVector(tbpStandard, authenticationDto.timeBetweenPresses());
        List<Integer> kptHammingVec = Utils.calcHammingVector(kptStandard, authenticationDto.keyPressTime());
        int hammingDistance = Utils.calcHammingDistance(tbpHammingVec) + Utils.calcHammingDistance(kptHammingVec);

        List<Map<Integer, Integer>> allTimeBetweenPresses = passwordService.findAllTimeBetweenPressesFor(passwordDto.id());
        List<Map<Integer, Integer>> allKeyPressTime = passwordService.findAllKeyPressTimeFor(passwordDto.id());

        List<Integer> hammingDistances = new ArrayList<>();
        for (int i = 0; i < allKeyPressTime.size(); i++)
        {
            int tbpDistance = Utils.calcHammingDistance(Utils.calcHammingVector(tbpStandard, allTimeBetweenPresses.get(i)));
            int kptDistance = Utils.calcHammingDistance(Utils.calcHammingVector(kptStandard, allKeyPressTime.get(i)));

            hammingDistances.add(tbpDistance + kptDistance);
        }

        double hammingDistanceThreshold = Utils.mathExpectedVal(hammingDistances) + STUDENT_COEFFICIENT * Utils.dispersion(hammingDistances);

        return hammingDistance <= hammingDistanceThreshold;
    }


    public Optional<UserReadDto> findByNickname(String nickname) {

        try {
            return userDao.findByNickname(nickname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
