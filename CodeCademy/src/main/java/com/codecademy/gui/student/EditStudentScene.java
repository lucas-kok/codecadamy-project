package com.codecademy.gui.student;

import com.codecademy.informationhandling.Student.StudentRepository;
import com.codecademy.informationhandling.Student.Student;
import com.codecademy.gui.GUI;
import com.codecademy.gui.GUIScene;
import com.codecademy.informationhandling.InformationHandler;
import com.codecademy.informationhandling.validators.StudentInformationValidator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class EditStudentScene extends GUIScene {
    private Scene editStudentScene;
    private final int sceneWidth;
    private final int sceneHeight;

    private final GUI gui;
    private final StudentInformationValidator informationValidationTools;
    private final InformationHandler informationHandler;
    private Student selectedStudent;

    public EditStudentScene(GUI gui, int sceneWidth, int sceneHeight, Student selectedStudent) {
        super(gui);

        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        this.gui = gui;
        informationValidationTools = new StudentInformationValidator();
        informationHandler = new InformationHandler();
        this.selectedStudent = selectedStudent;

        if (selectedStudent != null) {
            createScene();
            setScene(editStudentScene);
        }
    }

    private void createScene() {
        // Panes & Scene
        BorderPane mainPane = new BorderPane();
        VBox header = new VBox(15);
        HBox navigation = new HBox(15);
        VBox editStudentPane = new VBox(15);

        editStudentScene = new Scene(mainPane, sceneWidth, sceneHeight);

        header.setAlignment(Pos.CENTER);

        // Nodes
        Label title = new Label("Edit Student");
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
        DatePicker studentBirthdayInput = new DatePicker();

        Button updateSelectedStudentButton = new Button("Update Student");
        Label messageLabel = new Label();

        // Setting the TextFields to the students current info
        studentNameInput.setText(selectedStudent.getName());
        studentEmailInput.setText(selectedStudent.getEmail());
        studentAddressInput.setText(selectedStudent.getAddress());
        studentPostalCodeInput.setText(selectedStudent.getPostalCode());
        studentCityInput.setText(selectedStudent.getCity());
        studentCountryInput.setText(selectedStudent.getCountry());
        studentGenderInput.setValue(selectedStudent.getGender());
        studentBirthdayInput.setValue(selectedStudent.getBirthdayAsLocalDate());

        // Event Handlers
        homeButton.setOnAction((event) -> showScene("mainScene"));

        backButton.setOnAction((event) -> {
            ((OverviewStudentsScene) getSceneObject("overviewStudentsScene")).resetScene();
            showScene("overviewStudentsScene");
        });

        deleteStudentButton.setOnAction((event) -> {
            informationHandler.deleteStudent(selectedStudent);

            ((OverviewStudentsScene) getSceneObject("overviewStudentsScene")).resetScene();
            showScene("overviewStudentsScene");
        });

        updateSelectedStudentButton.setOnAction((event) -> {
            // Only proceed if all fields are filled in
            if (!studentNameInput.getText().isBlank() && !studentEmailInput.getText().isEmpty() && !studentAddressInput.getText().isEmpty() &&
                    !studentCityInput.getText().isEmpty() && !studentPostalCodeInput.getText().isEmpty() && !studentCountryInput.getText().isEmpty() &&
                    studentGenderInput.getValue() != null && studentBirthdayInput.getValue() != null) {
                String name = studentNameInput.getText();
                String email = studentEmailInput.getText();
                String address = studentAddressInput.getText();
                String postalCode = studentPostalCodeInput.getText();
                String city = studentCityInput.getText();
                String country = studentCountryInput.getText();
                String gender = studentGenderInput.getValue();
                LocalDate birthday = studentBirthdayInput.getValue();

                String response = informationValidationTools.validateEditedStudent(name, email, postalCode, birthday, gui.getStudents(), selectedStudent);
                messageLabel.setText(response);

                if (response.isBlank()) { // No errors, all inputs are valid
                    informationHandler.updateStudent(selectedStudent, name, email, address, postalCode, city, country, gender, birthday);

                    messageLabel.setText("The Student '" + name + "' has successfully been updated!");
                }

            } else {
                messageLabel.setText("Please fill in all the fields!");
            }
        });

        // Appending
        mainPane.setTop(header);
        mainPane.setCenter(editStudentPane);

        header.getChildren().addAll(title, navigation);
        navigation.getChildren().addAll(homeButton, backButton, deleteStudentButton);

        editStudentPane.getChildren().addAll(studentNameLabel, studentNameInput, studentEmailLabel, studentEmailInput,
                studentAddressLabel, studentAddressInput, studentPostalCodeLabel, studentPostalCodeInput, studentCityLabel,
                studentCityInput, studentCountryLabel, studentCountryInput, studentGenderLabel, studentGenderInput,
                studentBirthdayLabel, studentBirthdayInput, updateSelectedStudentButton, messageLabel);
    }

    public void resetScene(Student selectedStudent) {
        if (selectedStudent == null) return;

        this.selectedStudent = selectedStudent;
        createScene();
        setScene(editStudentScene);
    }

}
