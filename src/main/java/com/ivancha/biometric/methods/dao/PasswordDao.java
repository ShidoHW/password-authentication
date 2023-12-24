package com.ivancha.biometric.methods.dao;

import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.PasswordReadDto;
import com.ivancha.biometric.methods.dto.StandardDto;
import com.ivancha.biometric.methods.dto.StatisticCreateDto;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class PasswordDao {

    private final Connection dbConnection;


    public long create(PasswordCreateDto passwordCreateDto) throws SQLException {

        long passwordId = savePassword(new idValueDto(passwordCreateDto.userId(), passwordCreateDto.value()));
        savePasswordTbpStandard(passwordCreateDto.tbpStandard(), passwordId);
        savePasswordKptStandard(passwordCreateDto.kpsStandard(), passwordId);
        return passwordId;
    }

    public void create(StatisticCreateDto statisticCreateDto) throws SQLException {
        saveTimeBetweenPressesFor(statisticCreateDto.passwordId(), statisticCreateDto.timeBetweenPresses());
        saveKeyPressTimeFor(statisticCreateDto.passwordId(), statisticCreateDto.keyPressTime());
    }

    public PasswordReadDto findForUser(long userId) throws SQLException {

        idValueDto passwordForUser = findPasswordForUser(userId);

        return new PasswordReadDto(
                passwordForUser.id(),
                passwordForUser.value(),
                findTbpStandardFor(passwordForUser.id()),
                findKptStandardFor(passwordForUser.id())
        );

    }

    private void savePasswordKptStandard(Map<Integer, StandardDto> tbp, long passwordId) throws SQLException {

        for (Map.Entry<Integer, StandardDto> elemStandard : tbp.entrySet()) {
            try (PreparedStatement statement = dbConnection.prepareStatement("""
                    INSERT INTO kpt_standard(gap_number, min, max, password_id)
                    VALUES (?, ?, ?, ?)
                    """)) {
                statement.setInt(1, elemStandard.getKey());
                statement.setLong(2, elemStandard.getValue().min());
                statement.setLong(3, elemStandard.getValue().max());
                statement.setLong(4, passwordId);

                statement.execute();
            }
        }
    }

    private Map<Integer, StandardDto> findTbpStandardFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
            SELECT gap_number, min, max
            FROM tbp_standard
            WHERE password_id = ?
            """)) {

            statement.setLong(1, passwordId);
            try (ResultSet resultSet = statement.executeQuery()) {

                Map<Integer, StandardDto> tbpStandard = new HashMap<>();
                while (resultSet.next())
                {
                    tbpStandard.put(resultSet.getInt("gap_number"),
                            new StandardDto(resultSet.getInt("min"), resultSet.getInt("max")));
                }
                return tbpStandard;
            }
        }
    }

    private Map<Integer, StandardDto> findKptStandardFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
            SELECT gap_number, min, max
            FROM kpt_standard
            WHERE password_id = ?
            """)) {

            statement.setLong(1, passwordId);
            try (ResultSet resultSet = statement.executeQuery()) {

                Map<Integer, StandardDto> tbpStandard = new HashMap<>();
                while (resultSet.next())
                {
                    tbpStandard.put(resultSet.getInt("gap_number"),
                            new StandardDto(resultSet.getInt("min"), resultSet.getInt("max")));
                }
                return tbpStandard;
            }
        }
    }

    private void savePasswordTbpStandard(Map<Integer, StandardDto> tbp, long passwordId) throws SQLException {

        for (Map.Entry<Integer, StandardDto> elemStandard : tbp.entrySet()) {
            try (PreparedStatement statement = dbConnection.prepareStatement("""
                    INSERT INTO tbp_standard(gap_number, min, max, password_id)
                    VALUES (?, ?, ?, ?)
                    """)) {
                statement.setInt(1, elemStandard.getKey());
                statement.setLong(2, elemStandard.getValue().min());
                statement.setLong(3, elemStandard.getValue().max());
                statement.setLong(4, passwordId);

                statement.execute();
            }
        }
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


    public List<Map<Integer, Integer>> findAllTimeBetweenPressesFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT gap_number, time tbp_time
                FROM time_between_presses tbp
                WHERE password_id = ?
                ORDER BY gap_number;
            """)) {

            statement.setLong(1, passwordId);
            try(ResultSet resultSet = statement.executeQuery()) {

                boolean firstZeroNum = true;
                List<Map<Integer, Integer>> allTimeBetweenPresses = new ArrayList<>();
                Map<Integer, Integer> timeBetweenPresses = new HashMap<>();
                while (resultSet.next())
                {
                    int gupNumber = resultSet.getInt("gap_number");
                    if (!firstZeroNum && gupNumber == 0) {
                        allTimeBetweenPresses.add(timeBetweenPresses);
                        timeBetweenPresses = new HashMap<>();
                    }
                    timeBetweenPresses.put(gupNumber, resultSet.getInt("tbp_time"));
                    firstZeroNum = false;
                }
                allTimeBetweenPresses.add(timeBetweenPresses);

                return allTimeBetweenPresses;
            }
        }
    }


    public List<Map<Integer, Integer>> findAllKeyPressTimeFor(long passwordId) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT gap_number, time kpt_time
                FROM key_press_time kpt
                WHERE password_id = ?
                ORDER BY gap_number;
            """)) {

            statement.setLong(1, passwordId);
            try(ResultSet resultSet = statement.executeQuery()) {

                boolean firstZeroNum = true;
                List<Map<Integer, Integer>> allKeyPressTime = new ArrayList<>();
                Map<Integer, Integer> keyPressTime = new HashMap<>();
                while (resultSet.next())
                {
                    int gupNumber = resultSet.getInt("gap_number");
                    if (!firstZeroNum && gupNumber == 0) {
                        allKeyPressTime.add(keyPressTime);
                        keyPressTime = new HashMap<>();
                    }
                    keyPressTime.put(gupNumber, resultSet.getInt("kpt_time"));
                    firstZeroNum = false;
                }
                allKeyPressTime.add(keyPressTime);

                return allKeyPressTime;
            }
        }
    }

    private record idValueDto(long id, String value) { }
}
