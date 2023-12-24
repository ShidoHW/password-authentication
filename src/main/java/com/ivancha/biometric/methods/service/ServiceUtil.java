package com.ivancha.biometric.methods.service;

import com.ivancha.biometric.methods.DbManager;
import com.ivancha.biometric.methods.dao.PasswordDao;
import com.ivancha.biometric.methods.dao.UserDao;
import lombok.experimental.UtilityClass;


@UtilityClass
public class ServiceUtil {

    public static ServiceResources startServices() {

        DbManager manager = DbManager.getManager();

        PasswordDao passwordDao = new PasswordDao(manager.getConn());
        UserDao userDao = new UserDao(manager.getConn(), passwordDao);

        PasswordService passwordService = new PasswordService(passwordDao);
        UserService userService = new UserService(userDao, passwordService);

        return new ServiceResources(userService, passwordService);
    }

    public record ServiceResources(
            UserService userService,
            PasswordService passwordService
    ) {}

}
