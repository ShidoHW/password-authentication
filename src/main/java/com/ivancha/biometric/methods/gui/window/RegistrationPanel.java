package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.PasswordReadDto;
import com.ivancha.biometric.methods.dto.UserCreateDto;
import com.ivancha.biometric.methods.dto.UserReadDto;
import com.ivancha.biometric.methods.gui.listener.KeyStatisticsCollector;
import com.ivancha.biometric.methods.service.PasswordService;
import com.ivancha.biometric.methods.service.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegistrationPanel extends JPanel {

    public RegistrationPanel(UserService userService, PasswordService passwordService) {

        super(new MigLayout());

        JLabel registerLbl = new JLabel("Зарегистрируемся");
        this.add(registerLbl, "span, center, gapbottom 15");

        JLabel usernameLbl = new JLabel("Введите имя пользователя: ");
        JTextField usernameTextField = new JTextField(20);
        this.add(usernameLbl);
        this.add(usernameTextField, "wrap");

        JLabel passwordLbl = new JLabel("Введите пароль: ");
        this.add(passwordLbl);
        JTextField passwordTextField = new JTextField(10); // принимает до 10 символов
        KeyStatisticsCollector keyStatistics = new KeyStatisticsCollector();
        passwordTextField.addKeyListener(keyStatistics);
        this.add(passwordTextField, "wrap");

        var sendButtonListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                var userId = userService.create(new UserCreateDto(usernameTextField.getText()));

                List<Long> timeBetweenPresses = keyStatistics.getTimeBetweenPresses();
                timeBetweenPresses.remove(0);
                passwordService.create(new PasswordCreateDto(
                        passwordTextField.getText(),
                        userId,
                        listToMapWithOrder(timeBetweenPresses),
                        listToMapWithOrder(keyStatistics.getPressTimeList()))
                );

                usernameTextField.setText("");
                passwordTextField.setText("");
                keyStatistics.clear();
            }

            public Map<Integer, Integer> listToMapWithOrder(List<Long> list) {
                Map<Integer, Integer> map = new HashMap<>();

                for (int i = 0; i < list.size(); i++)
                    map.put(i, list.get(i).intValue());

                return map;
            }
        };

        JButton sendBtn = new JButton("Отправить");
        sendBtn.addActionListener(sendButtonListener);
        this.add(sendBtn, "span, gapbottom 15");
    }
}
