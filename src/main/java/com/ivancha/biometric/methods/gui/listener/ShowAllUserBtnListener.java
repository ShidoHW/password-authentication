package com.ivancha.biometric.methods.gui.listener;

import com.ivancha.biometric.methods.dto.PasswordReadDto;
import com.ivancha.biometric.methods.dto.UserReadDto;
import com.ivancha.biometric.methods.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;


@RequiredArgsConstructor
public class ShowAllUserBtnListener extends AbstractAction {

    private final JPanel parentComponent;

    private final UserService userService;

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        var userInfoList = userService.findAll();

        for (int i = 0; i < userInfoList.size(); i++) {

            UserReadDto userDto = userInfoList.get(i);
            PasswordReadDto passwordDto = userDto.passwordReadDto();
            sb.append(String.format("%d: Имя пользователя: %-15s Пароль: %-10s Время нажатия клавиш: %-50S Время между нажатиями клавиш: %-50s",
                    i,
                    userDto.nickname(),
                    passwordDto.value(),
                    passwordDto.keyPressTime(),
                    passwordDto.timeBetweenPresses())
            );
            sb.append('\n');

        }
        JOptionPane.showMessageDialog(parentComponent, sb.toString());
    }
}
