package com.codecademy;

import com.codecademy.gui.GUI;
import javafx.application.Application;

public class Main {

    // Function to start the Application
    public static void main(String[] args) {
        GUI gui = new GUI();
        Application.launch(GUI.class);
    }
}
