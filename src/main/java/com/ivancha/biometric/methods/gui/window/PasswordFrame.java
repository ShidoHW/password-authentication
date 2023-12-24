package com.ivancha.biometric.methods.gui.window;

import com.ivancha.biometric.methods.DbManager;
import com.ivancha.biometric.methods.service.ServiceUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class PasswordFrame extends JFrame {


    public PasswordFrame(ServiceUtil.ServiceResources sr) {

        super("Пароли, парольчики, паролища");

        JPanel parentPanel = new JPanel(new MigLayout());

        JPanel regPanel = new RegistrationPanel(sr.userService(), sr.passwordService());
        parentPanel.add(regPanel, "wrap");

        AllUserPanel allUserPanel = new AllUserPanel(sr.userService());
        parentPanel.add(allUserPanel, "wrap");

        AuthenticationPanel authenticationPanel = new AuthenticationPanel(sr.userService());
        parentPanel.add(authenticationPanel);

        this.add(parentPanel);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                DbManager.getManager().closeConnection();
            }
        });
    }
}
