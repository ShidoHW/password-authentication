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

        UserService userService = new UserService(userDao);
        PasswordService passwordService = new PasswordService(passwordDao);

        return new ServiceResources(userService, passwordService);
    }

    public record ServiceResources(
            UserService userService,
            PasswordService passwordService
    ) {}

}
