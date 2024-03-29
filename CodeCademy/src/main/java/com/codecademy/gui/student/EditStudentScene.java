package com.codecademy.gui.student;

import com.codecademy.gui.GUI;
import com.codecademy.gui.GUIScene;
import com.codecademy.informationhandling.student.StudentRepository;
import com.codecademy.informationhandling.student.Student;
import com.codecademy.informationhandling.validators.StudentInformationValidator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class EditStudentScene extends GUIScene {
    private Scene editStudentScene;
    private final int sceneWidth;
    private final int sceneHeight;

    private final StudentInformationValidator informationValidationTools;
    private final StudentRepository studentRepository;
    private Student selectedStudent;

    public EditStudentScene(GUI gui, int sceneWidth, int sceneHeight) {
        super(gui);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        informationValidationTools = new StudentInformationValidator();
        studentRepository = new StudentRepository();

        // Not creating a scene, because when initializing the GUI no Student has been selected
    }

    private void createScene() {
        // Panes & Scene
        BorderPane mainPane = new BorderPane();
        VBox headerPane = new VBox(15);
        HBox navigationPane = new HBox(15);
        VBox editStudentPane = new VBox(15);
        HBox editStudentBirthdayPane = new HBox(5);

        editStudentScene = new Scene(mainPane, sceneWidth, sceneHeight);

        headerPane.setAlignment(Pos.CENTER);

        // Nodes
        Label titleLabel = new Label("Edit Student: " + selectedStudent.getEmail());
        Button homeButton = new Button("Home");
        Button backButton = new Button("Back");
        Button deleteStudentButton = new Button("Delete");

        Label studentNameLabel = new Label("Name:");
        TextField studentNameInput = new TextField();

        Label studentEmailLabel = new Label("Email:");
        TextField studentEmailInput = new TextField();

        Label studentAddressLabel = new Label("Address:");
        TextField studentAddressInput = new TextField();

        Label studentPostalCodeLabel = new Label("Postal Code:");
        TextField studentPostalCodeInput = new TextField();

        Label studentCityLabel = new Label("City:");
        TextField studentCityInput = new TextField();

        Label studentCountryLabel = new Label("Country:");
        TextField studentCountryInput = new TextField();

        Label studentGenderLabel = new Label("Gender:");
        ComboBox<String> studentGenderInput = new ComboBox<>();
        studentGenderInput.getItems().add("Male");
        studentGenderInput.getItems().add("Female");
        studentGenderInput.getItems().add("Other");

        Label studentBirthdayLabel = new Label("Birthday:");
        TextField studentBirthdayDayInput = new TextField();
        TextField studentBirthdayMonthInput = new TextField();
        TextField studentBirthdayYearInput = new TextField();

        Button updateSelectedStudentButton = new Button("Update Student");
        Label messageLabel = new Label();

        // Setting the TextFields to the info of the selected Student
        studentNameInput.setText(selectedStudent.getName());
        studentEmailInput.setText(selectedStudent.getEmail());
        studentAddressInput.setText(selectedStudent.getAddress());
        studentPostalCodeInput.setText(selectedStudent.getPostalCode());
        studentCityInput.setText(selectedStudent.getCity());
        studentCountryInput.setText(selectedStudent.getCountry());
        studentGenderInput.setValue(selectedStudent.getGender());

        String[] selectedUserBirthdayPieces = selectedStudent.getBirthdayPieces();
        studentBirthdayDayInput.setText(selectedUserBirthdayPieces[0]);
        studentBirthdayMonthInput.setText(selectedUserBirthdayPieces[1]);
        studentBirthdayYearInput.setText(selectedUserBirthdayPieces[2]);

        studentBirthdayDayInput.setPromptText("Day");
        studentBirthdayMonthInput.setPromptText("Month");
        studentBirthdayYearInput.setPromptText("Year");

        // Styling
        editStudentScene.setUserAgentStylesheet("/style.css");
        headerPane.setId("header");
        titleLabel.setId("title");
        navigationPane.setId("navigation");
        studentGenderInput.setId("clickable");
        updateSelectedStudentButton.setId("actionButton");
        messageLabel.setId("errorMessage");

        // Event Handlers
        homeButton.setOnAction((event) -> showScene("mainScene"));

        backButton.setOnAction((event) -> {
            ((ViewStudentScene) getSceneObject("viewStudentScene")).resetScene(selectedStudent);
            showScene("viewStudentScene");
        });

        deleteStudentButton.setOnAction((event) -> {
            studentRepository.deleteStudent(selectedStudent);

            ((OverviewStudentsScene) getSceneObject("overviewStudentsScene")).resetScene();
            showScene("overviewStudentsScene");
        });

        updateSelectedStudentButton.setOnAction((event) -> {
            // Only proceed if all fields are filled in
            if (!studentNameInput.getText().isBlank() && !studentEmailInput.getText().isEmpty() && !studentAddressInput.getText().isEmpty() && !studentCityInput.getText().isEmpty() &&
                    !studentPostalCodeInput.getText().isEmpty() && !studentCountryInput.getText().isEmpty() &&studentGenderInput.getValue() != null &&
                    !studentBirthdayDayInput.getText().isBlank() && !studentBirthdayMonthInput.getText().isBlank() && !studentBirthdayYearInput.getText().isBlank()) {
                String name = studentNameInput.getText();
                String email = studentEmailInput.getText();
                String address = studentAddressInput.getText();
                String postalCode = studentPostalCodeInput.getText();
                String city = studentCityInput.getText();
                String country = studentCountryInput.getText();
                String gender = studentGenderInput.getValue();

                String birthdayDay = studentBirthdayDayInput.getText();
                String birthdayMonth = studentBirthdayMonthInput.getText();
                String birthdayYear = studentBirthdayYearInput.getText();
                String[] birthdayPieces = new String[] { birthdayDay, birthdayMonth, birthdayYear };

                String response = null;
                try {
                    response = informationValidationTools.validateEditedStudent(name, email, address, postalCode, birthdayPieces, selectedStudent);

                    messageLabel.setId("errorMessage");
                    messageLabel.setText(response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                assert response != null;
                if (response.isBlank()) { // No errors, all inputs are valid
                    // Update Student
                    String birthday = birthdayPieces[2] + "-" + birthdayPieces[1] + "-" + birthdayPieces[0];
                    studentRepository.updateStudent(selectedStudent, name, email, address, postalCode, city, country, gender, birthday);

                    messageLabel.setId("goodMessage");
                    messageLabel.setText("\nThe Student '" + name + "' has successfully been updated!");
                }

            } else {
                messageLabel.setId("errorMessage");
                messageLabel.setText("\nPlease fill in all the fields!");
            }
        });

        // Appending
        mainPane.setTop(headerPane);
        mainPane.setCenter(editStudentPane);

        headerPane.getChildren().addAll(titleLabel, navigationPane);
        navigationPane.getChildren().addAll(homeButton, backButton, deleteStudentButton);

        editStudentPane.getChildren().addAll(studentNameLabel, studentNameInput, studentEmailLabel, studentEmailInput,
                studentAddressLabel, studentAddressInput, studentPostalCodeLabel, studentPostalCodeInput, studentCityLabel,
                studentCityInput, studentCountryLabel, studentCountryInput, studentGenderLabel, studentGenderInput,
                studentBirthdayLabel, editStudentBirthdayPane, updateSelectedStudentButton, messageLabel);

        editStudentBirthdayPane.getChildren().addAll(studentBirthdayDayInput, studentBirthdayMonthInput, studentBirthdayYearInput);
    }

    public void resetScene(Student selectedStudent) {
        if (selectedStudent == null) return;

        this.selectedStudent = selectedStudent;

        createScene();
        setScene(editStudentScene);
    }
}
