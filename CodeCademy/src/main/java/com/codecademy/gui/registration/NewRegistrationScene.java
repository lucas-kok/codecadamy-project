package com.codecademy.gui.registration;

import com.codecademy.gui.GUI;
import com.codecademy.gui.GUIScene;
import com.codecademy.gui.student.OverviewStudentsScene;
import com.codecademy.informationhandling.InformationHandler;
import com.codecademy.informationhandling.validators.RegistrationInformationValidator;
import com.codecademy.informationhandling.validators.StudentInformationValidator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class NewRegistrationScene extends GUIScene {
    private Scene newRegistrationScene;
    private final int sceneWidth;
    private final int sceneHeight;

    private final GUI gui;
    private final RegistrationInformationValidator registrationInformationValidator;
    private final InformationHandler informationHandler;

    public NewRegistrationScene(GUI gui, int sceneWidth, int sceneHeight) {
        super(gui);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        this.gui = gui;
        registrationInformationValidator = new RegistrationInformationValidator();
        informationHandler = new InformationHandler();

        createScene();
        setScene(newRegistrationScene);
    }

    private void createScene() {
        // Panes & Scene
        BorderPane mainPane = new BorderPane();
        VBox header = new VBox(15);
        HBox navigation = new HBox(15);
        VBox newRegistrationPane = new VBox(15);

        newRegistrationScene = new Scene(mainPane, sceneWidth, sceneHeight);

        header.setAlignment(Pos.CENTER);

        // Nodes
        Label title = new Label("Create new Registration");
        Button homeButton = new Button("Home");
        Button backButton = new Button("Back");

        Label registrationDateLabel = new Label("Registration Date:");
        DatePicker registrationDateInput = new DatePicker();

        Label registrationStudentEmailLabel = new Label("Student Email:");
        TextField registrationStudentEmailInput = new TextField();

        Label registrationCourseNameLabel = new Label("Course Name:");
        TextField registrationCourseNameInput = new TextField();

        Button createRegistrationButton = new Button("Create");
        Label messageLabel = new Label("");

        // Event Handlers
        homeButton.setOnAction((event) -> showScene("mainScene"));

        backButton.setOnAction((event) -> {
            ((OverviewRegistrationsScene) getSceneObject("overviewRegistrationsScene")).resetScene();
            showScene("overviewRegistrationsScene");
        });

        createRegistrationButton.setOnAction((event) -> {
            // Only proceed if all fields are filled in
            if (registrationDateInput.getValue() != null && !registrationStudentEmailInput.getText().isBlank() && !registrationCourseNameInput.getText().isBlank()) {
                LocalDate registrationDate = registrationDateInput.getValue();
                String studentEmail = registrationStudentEmailInput.getText();
                String courseName = registrationCourseNameInput.getText();

                String response = registrationInformationValidator.validateNewRegistration(studentEmail, courseName, gui.getStudents(), gui.getCourses());
                messageLabel.setText(response);

                if (response.isBlank()) { // No errors, all inputs are valid
                    // Create new Registration

                    // Clearing all fields
                    registrationDateInput.setValue(null);
                    registrationStudentEmailInput.clear();
                    registrationCourseNameInput.clear();

                    messageLabel.setText("The Registration for '" + studentEmail + "' has successfully been created!");
                }

            } else {
                messageLabel.setText("Please fill in all the fields!");
            }
        });

        // Appending
        mainPane.setTop(header);
        mainPane.setCenter(newRegistrationPane);

        header.getChildren().addAll(title, navigation);
        navigation.getChildren().addAll(homeButton, backButton);

        newRegistrationPane.getChildren().addAll(registrationDateLabel, registrationDateInput, registrationStudentEmailLabel,
                registrationStudentEmailInput, registrationCourseNameLabel, registrationCourseNameInput, createRegistrationButton, messageLabel);
    }

    public void resetScene() {
        createScene();
        setScene(newRegistrationScene);
    }

}