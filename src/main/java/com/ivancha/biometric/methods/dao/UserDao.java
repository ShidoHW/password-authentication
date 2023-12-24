package com.ivancha.biometric.methods.dao;

import com.ivancha.biometric.methods.dto.PasswordReadDto;
import com.ivancha.biometric.methods.dto.UserReadDto;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class UserDao {

    private final Connection dbConnection;

    private final PasswordDao passwordDao;


    public Optional<UserReadDto> findByNickname(String username) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT id, nickname
                FROM users u
                WHERE nickname = ?;
                    """)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                {
                    long id = resultSet.getLong("id");
                    PasswordReadDto passwordReadDto = passwordDao.findForUser(id);
                    return Optional.of(new UserReadDto(id, username, passwordReadDto));
                }
                else
                    return Optional.empty();
            }
        }
    }


    public Long create(String username) throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                    INSERT INTO users(nickname)
                    VALUES (?)
                    RETURNING id as user_id
                    """)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                return resultSet.getLong("user_id");
            }
        }
    }


    public List<UserReadDto> findAll() throws SQLException {

        try (PreparedStatement statement = dbConnection.prepareStatement("""
                SELECT id, nickname
                FROM users
            """);
             ResultSet resultSet = statement.executeQuery()) {

            List<UserReadDto> userReadDtos = new ArrayList<>();
            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String username = resultSet.getString("nickname");
                PasswordReadDto passwordReadDto = passwordDao.findForUser(id);
                userReadDtos.add(new UserReadDto(id, username, passwordReadDto));
            }

            return userReadDtos;
        }
    }
}
