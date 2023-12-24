package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.dto.AuthenticationDto;
import com.ivancha.biometric.methods.dto.UserReadDto;
import com.ivancha.biometric.methods.gui.listener.KeyStatisticsCollector;
import com.ivancha.biometric.methods.service.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;


public class AuthenticationPanel extends JPanel {

    public AuthenticationPanel(UserService userService) {

        super(new MigLayout());

        JLabel registerLbl = new JLabel("Идентифицируемся");
        this.add(registerLbl, "span, center, gapbottom 15");

        JLabel usernameLbl = new JLabel("Как звать: ");
        JTextField usernameTextField = new JTextField(15);
        this.add(usernameLbl);
        this.add(usernameTextField, "wrap");

        JLabel passwordLbl = new JLabel("Каков пароль: ");
        this.add(passwordLbl);
        JTextField passwordTextField = new JTextField(10);
        KeyStatisticsCollector passwordKeyStatistics = new KeyStatisticsCollector();
        passwordTextField.addKeyListener(passwordKeyStatistics);
        this.add(passwordTextField, "wrap");

        var sendButtonListener = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

            String username = usernameTextField.getText();

            Optional<UserReadDto> mayBeUser = userService.findByNickname(username);

            if(mayBeUser.isEmpty()) {
                JOptionPane.showMessageDialog(AuthenticationPanel.this, "Ты кто такой? Я тебя не звал и не нашёл тоже");
                clearPasswordField();
                return;
            }
            else {
                String password = passwordTextField.getText();
                List<Integer> timeBetweenPresses = passwordKeyStatistics.getTimeBetweenPresses();
                timeBetweenPresses.remove(0);

                if (userService.isInfCorrectForUser(new AuthenticationDto(
                        username,
                        password,
                        timeBetweenPresses,
                        passwordKeyStatistics.getPressTimeList()))
                ) {
                    JOptionPane.showMessageDialog(AuthenticationPanel.this,
                            "Всегда рад тебе, " + username);
                    usernameTextField.setText("");
                }


                else {
                    JOptionPane.showMessageDialog(AuthenticationPanel.this,
                            "Ты лишь притворяешься моим другом, уходи НЕ " + username);
                }
            }

            clearPasswordField();
            }

            private void clearPasswordField() {
                passwordTextField.setText("");
                passwordKeyStatistics.clear();
            }

        };

        JButton sendBtn = new JButton("Отправить");
        sendBtn.addActionListener(sendButtonListener);
        this.add(sendBtn);
    }
}
