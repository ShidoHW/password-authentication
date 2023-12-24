package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.dto.PasswordCreateDto;
import com.ivancha.biometric.methods.dto.StandardDto;
import com.ivancha.biometric.methods.dto.StatisticCreateDto;
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

import static com.ivancha.biometric.methods.Utils.getMinMaxElemByElem;


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
        passErrField.addKeyListener(passErrKeyStatistics);
        this.add(passErrField, "wrap");

        var sendButtonListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameTextField.getText();

                if (userService.findByNickname(username).isPresent()) {
                    JOptionPane.showMessageDialog(RegistrationPanel.this,
                            "Так не пойдёт. Это имя пользователя занято. Возьмите другое!");
                    clearFields();
                    return;
                }

                String pass = passwordTextField.getText();
                String confirmPass = confirmTextField.getText();
                String errPass = passErrField.getText();

                if (!pass.equals(confirmPass) || !pass.equals(errPass)) {
                    JOptionPane.showMessageDialog(RegistrationPanel.this,
                            "Так не пойдёт. Пароли не совпадают. Заново!");
                    clearFields();
                    return;
                }
                if (pass.length() < 1)
                {
                    JOptionPane.showMessageDialog(RegistrationPanel.this,
                            "Так не пойдёт. Хочу пароль хотя бы из двух символов. Заново!");
                    clearFields();
                    return;
                }

                var userId = userService.create(new UserCreateDto(username));

                List<Integer> passTimeBetweenPresses = passwordKeyStatistics.getTimeBetweenPresses();
                passTimeBetweenPresses.remove(0);
                List<Integer> confirmTimeBetweenPresses = confirmKeyStatistics.getTimeBetweenPresses();
                confirmTimeBetweenPresses.remove(0);

                Map<Integer, StandardDto> tbpStandard = getMinMaxElemByElem(List.of(passTimeBetweenPresses, confirmTimeBetweenPresses));
                Map<Integer, StandardDto> kpsStandard = getMinMaxElemByElem(List.of(passwordKeyStatistics.getPressTimeList(), confirmKeyStatistics.getPressTimeList()));

                Long passwordId = passwordService.save(new PasswordCreateDto(
                        pass,
                        userId,
                        tbpStandard,
                        kpsStandard)
                );

                List<Integer> errTimeBetweenPresses = passErrKeyStatistics.getTimeBetweenPresses();
                errTimeBetweenPresses.remove(0);
                passwordService.save(new StatisticCreateDto(
                        passwordId,
                        listToMapWithOrder(errTimeBetweenPresses),
                        listToMapWithOrder(passErrKeyStatistics.getPressTimeList()))
                );

                clearFields();
            }

            private void clearFields() {
                usernameTextField.setText("");
                passwordTextField.setText("");
                confirmTextField.setText("");
                passErrField.setText("");
                passwordKeyStatistics.clear();
                confirmKeyStatistics.clear();
                passErrKeyStatistics.clear();
            }

            private Map<Integer, Integer> listToMapWithOrder(List<Integer> list) {
                Map<Integer, Integer> map = new HashMap<>();

                for (int i = 0; i < list.size(); i++)
                    map.put(i, list.get(i));

                return map;
            }
        };

        JButton sendBtn = new JButton("Отправить");
        sendBtn.addActionListener(sendButtonListener);
        this.add(sendBtn, "span, gapbottom 15");
    }
}
