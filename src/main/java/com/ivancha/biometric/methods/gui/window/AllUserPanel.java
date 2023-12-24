package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.gui.listener.ShowAllUserBtnListener;
import com.ivancha.biometric.methods.service.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;


public class AllUserPanel extends JPanel {

    public AllUserPanel(UserService userService) {

        super(new MigLayout());

        JButton showAllUsersBtn = new JButton("Показать всех зарегистрированных пользователей");
        showAllUsersBtn.addActionListener(new ShowAllUserBtnListener(this, userService));
        this.add(showAllUsersBtn, "span");
    }
}
