package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.UserCreateDto;
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
        JTextField usernameTextField = new JTextField(15);
        this.add(usernameLbl);
        this.add(usernameTextField, "wrap");

        JLabel passwordLbl = new JLabel("Введите пароль: ");
        this.add(passwordLbl);
        JTextField passwordTextField = new JTextField(10); // принимает до 10 символов
        KeyStatisticsCollector passwordKeyStatistics = new KeyStatisticsCollector();
        passwordTextField.addKeyListener(passwordKeyStatistics);
        this.add(passwordTextField, "wrap");

        JLabel confirmLbl = new JLabel("Подтвердите пароль: ");
        this.add(confirmLbl);

        JTextField confirmTextField = new JTextField(10); // принимает до 10 символов
        KeyStatisticsCollector confirmKeyStatistics = new KeyStatisticsCollector();
        confirmTextField.addKeyListener(confirmKeyStatistics);
        this.add(confirmTextField, "wrap");

        JLabel passErrLbl = new JLabel("Ещё раз, очень надо: ");
        this.add(passErrLbl);

        JTextField passErrField = new JTextField(10); // принимает до 10 символов
        KeyStatisticsCollector passErrKeyStatistics = new KeyStatisticsCollector();
        passErrField.addKeyListener(confirmKeyStatistics);
        this.add(passErrField, "wrap");

        var sendButtonListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                var userId = userService.create(new UserCreateDto(usernameTextField.getText()));

                List<Long> timeBetweenPresses = passwordKeyStatistics.getTimeBetweenPresses();
                timeBetweenPresses.remove(0);
                passwordService.create(new PasswordCreateDto(
                        passwordTextField.getText(),
                        userId,
                        listToMapWithOrder(timeBetweenPresses),
                        listToMapWithOrder(passwordKeyStatistics.getPressTimeList()))
                );

                usernameTextField.setText("");
                passwordTextField.setText("");
                confirmTextField.setText("");
                passErrField.setText("");
                passwordKeyStatistics.clear();
                confirmKeyStatistics.clear();
                passErrKeyStatistics.clear();
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
