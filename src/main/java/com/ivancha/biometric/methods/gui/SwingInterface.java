package com.ivancha.biometric.methods.gui;

import com.ivancha.biometric.methods.gui.window.PasswordFrame;

import static com.ivancha.biometric.methods.service.ServiceUtil.startServices;


public class SwingInterface {


    public static void main(String[] args) {

        var serviceResources = startServices();

        var frame = new PasswordFrame(serviceResources);
        frame.setVisible(true);
    }

}

