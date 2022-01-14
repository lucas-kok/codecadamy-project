package com.codecademy.informationhandling.validators;

import com.codecademy.informationhandling.InformationFormatter;
import com.codecademy.informationhandling.course.Course;
import com.codecademy.informationhandling.registration.Registration;
import com.codecademy.informationhandling.student.Student;

import java.util.ArrayList;
import java.util.Map;

public class RegistrationInformationValidator {
    InformationFormatter informationFormatter;

    public RegistrationInformationValidator() {
        informationFormatter = new InformationFormatter();
    }

    public String validateNewRegistration(String studentEmail, String courseName, Map<String, Student> students, Map<String, Course> courses, Map<String, Registration> registrations) {
        StringBuilder message = new StringBuilder();

        if (!validateStudentEmail(studentEmail, students)) message.append("\nThe email: '").append(studentEmail).append("' does not exists!");
        if (!validateCourseName(courseName, courses)) message.append("\nThe Course: '").append(courseName).append("' does not exists!");
        if (!registrationDoesNotExists(studentEmail, courseName, registrations)) message.append("The registration already exists!");

        return message.toString();
    }

    public String validateEditedRegistration(String[] registrationDatePieces, ArrayList<Integer> progression) {
        StringBuilder message = new StringBuilder();

        if (!validateRegistrationDate(registrationDatePieces)) message.append("The Registration Date is not valid!");
        if (!validateProgression(progression)) message.append("Some progression values are not between 0 and 100!");

        return message.toString();
    }

    // Registration Date
    public boolean validateRegistrationDate(String[] registrationDatePieces) {
        int day = Integer.parseInt(registrationDatePieces[2]);
        int month = Integer.parseInt(registrationDatePieces[1]);
        int year = Integer.parseInt(registrationDatePieces[0]);

        if ((day < 1 || day > 31) || (month < 1 || month >= 12)) return false;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if (day >= 1 && day <= 31) return true;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day <= 30) return true;
        } else if (month == 2) {
            if (year % 4 == 0) {
                if (!(year % 100 == 0 && year % 400 != 0)) {
                    if (day <= 29) return true;
                }
            }
            if (day <= 28) return true;
        }
        return false;
    }

    // Student email
    private boolean validateStudentEmail(String studentEmail, Map<String, Student> students) {
        return students.get(studentEmail.toLowerCase()) != null;
    }

    // Course name
    private boolean validateCourseName(String courseName, Map<String, Course> courses) {
        return courses.get(informationFormatter.capitalizeString(courseName)) != null;
    }

    private boolean registrationDoesNotExists(String studentEmail, String courseName, Map<String, Registration> registration) {
        return registration.get(studentEmail + "-" + courseName) == null;
    }

    // Progression
    private boolean validateProgression(ArrayList<Integer> progression) {
        for (int progress : progression) {
            if (progress < 0 || progress > 100) {
                return false;
            }
        }

        return true;
    }
}
