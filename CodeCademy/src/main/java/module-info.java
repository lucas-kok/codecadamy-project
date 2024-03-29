module com.example.codecademy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires junit;


    opens com.codecademy to javafx.fxml;
    exports com.codecademy;
    exports com.codecademy.gui;
    opens com.codecademy.gui to javafx.fxml;
    exports com.codecademy.gui.student;
    opens com.codecademy.gui.student to javafx.fxml;
    exports com.codecademy.informationhandling;
    opens com.codecademy.informationhandling to javafx.fxml;
    exports com.codecademy.informationhandling.validators;
    opens com.codecademy.informationhandling.validators to javafx.fxml;
    exports com.codecademy.gui.statistics;
    opens com.codecademy.gui.statistics to javafx.fxml;
    exports com.codecademy.tests;
    opens com.codecademy.tests to junit;
    exports com.codecademy.informationhandling.course;
    exports com.codecademy.informationhandling.student;
    exports com.codecademy.gui.home;
    opens com.codecademy.gui.home to javafx.fxml;
    exports com.codecademy.informationhandling.certificate;
    exports com.codecademy.informationhandling.registration;
    exports com.codecademy.informationhandling.contentitem;
}