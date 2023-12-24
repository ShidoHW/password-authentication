package com.ivancha.biometric.methods.dao;

import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.PasswordReadDto;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class PasswordDao {

    private final Connection dbConnection;


    public Long create(PasswordCreateDto passwordCreateDto) throws SQLException {

        long passwordId = savePassword(new idValueDto(passwordCreateDto.userId(), passwordCreateDto.value()));
        saveTimeBetweenPressesFor(passwordId, passwordCreateDto.timeBetweenPresses());
        saveKeyPressTimeFor(passwordId, passwordCreateDto.keyPressTime());

        return passwordId;
    }


    public PasswordReadDto findForUser(long userId) throws SQLException {

        idValueDto passwordForUser = findPasswordForUser(userId);

        return new PasswordReadDto(
                passwordForUser.id(),
                passwordForUser.value(),
                findTimeBetweenPressesFor(passwordForUser.id()),
                findKeyPressTimeFor(passwordForUser.id())
        );

    }


    private long savePassword(idValueDto userIdPassword) throws SQLException {

        try (PreparedStatement statement1 = dbConnection.prepareStatement("""
                    INSERT INTO password(value, user_id)
                    VALUES (?, ?)
                    RETURNING id as password_id
                    """)) {

            statement1.setString(1, userIdPassword.value());
            statement1.setLong(2, userIdPassword.id());
            try(ResultSet resultSet = statement1.executeQuery()) {

                resultSet.next();
                return resultSet.getInt("password_id");
            }
        }
    }


    private void saveTimeBetweenPressesFor(long passwordId, Map<Integer, Integer> timeBetweenPresses) throws SQLException {

        for (Map.Entry<Integer, Integer> entry : timeBetweenPresses.entrySet()) {

            try (PreparedStatement statement2 = dbConnection.prepareStatement("""
                    INSERT INTO time_between_presses(gap_number, time, password_id)
                    VALUES (?, ?, ?)
                    """)) {

                statement2.setInt(1, entry.getKey());
                statement2.setLong(2, entry.getValue());
                statement2.setLong(3, passwordId);

                statement2.execute();
            }
        }
    }


    private void saveKeyPressTimeFor(long passwordId, Map<Integer, Integer> keyPressTime) throws SQLException {

        for (Map.Entry<Integer, Integer> entry : keyPressTime.entrySet()) {

            try (PreparedStatement statement2 = dbConnection.prepareStatement("""
                    INSERT INTO key_press_time(gap_number, time, password_id)
                    VALUES (?, ?, ?)
                    """)) {

                statement2.setInt(1, entry.getKey());
                statement2.setLong(2, entry.getValue());
                statement2.setLong(3, passwordId);

                statement2.execute();
            }
        }
    }


    private idValueDto findPasswordForUser(long userId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT p.id psw_id, value
                FROM password p
                WHERE user_id = ?;
            """)) {

            statement.setLong(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                return new idValueDto(resultSet.getLong("psw_id"),
                        resultSet.getString("value"));
            }
        }
    }


    private Map<Integer, Integer> findTimeBetweenPressesFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT time tbp_time
                FROM time_between_presses tbp
                WHERE password_id = ?
                ORDER BY gap_number;
            """)) {

            statement.setLong(1, passwordId);
            try(ResultSet resultSet = statement.executeQuery()) {

                Map<Integer, Integer> timeBetweenPresses = new HashMap<>();
                int gapNumber = 0;
                while (resultSet.next()) {
                    timeBetweenPresses.put(gapNumber++, resultSet.getInt("tbp_time"));
                }

                return timeBetweenPresses;
            }
        }
    }


    private Map<Integer, Integer> findKeyPressTimeFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT time kpt_time
                FROM key_press_time kpt
                WHERE password_id = ?
                ORDER BY gap_number;
            """)) {

            statement.setLong(1, passwordId);
            try(ResultSet resultSet = statement.executeQuery()) {

                Map<Integer, Integer> timeBetweenPresses = new HashMap<>();
                int gapNumber = 0;

                while (resultSet.next()) {
                    timeBetweenPresses.put(gapNumber++, resultSet.getInt("kpt_time"));
                }

                return timeBetweenPresses;
            }
        }
    }


    private record idValueDto(long id, String value) { }
}
