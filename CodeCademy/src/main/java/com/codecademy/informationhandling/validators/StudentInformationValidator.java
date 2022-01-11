package com.codecademy.informationhandling.validators;

import com.codecademy.Student;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

public class StudentInformationValidator {

    public StudentInformationValidator() {

    }

    // Function to validate all inputs for a new Student and returns an error/empty message
    public String validateNewStudent(String name, String email, String postalCode, LocalDate birthday, Map<String, Student> students) {
        StringBuilder message = new StringBuilder();

        if (!validateName(name)) message.append("\nPlease fill in the First and Last name!");
        if (!validateMailAddress(email)) message.append("\nThe email: '").append(email).append("' is not valid!");
        if (!mailAddressDoesNotExist(email, students))
            message.append("\nThe email: ").append(email).append(" already exists!");
        if (!validatePostalCode(postalCode))
            message.append("\nThe postal-code: '").append(postalCode).append("' is not valid!");
        if (!validateAge(birthday)) message.append("\nThe person is not old enough!");

        return message.toString();
    }

    // Function to validate all inputs for an existing Student and returns an error/empty message
    public String validateEditedStudent(String name, String email, String postalCode, LocalDate birthday, Map<String, Student> students, Student selectedStudent) {
        StringBuilder message = new StringBuilder();

        if (!validateName(name)) message.append("\nPlease fill in both First- and Last name!");
        if (!validateMailAddress(email)) message.append("\nThe email: '").append(email).append("' is not valid!");
        if (!email.equals(selectedStudent.getEmail())) { // Student already has his own email (Already exists, but still valid)
            if (!mailAddressDoesNotExist(email, students))
                message.append("\nThe email: ").append(email).append(" already exists!");
        }
        if (!validatePostalCode(postalCode))
            message.append("\nThe postal-code: '").append(postalCode).append("' is not valid!");
        if (!validateAge(birthday)) message.append("\nThe person is not old enough!");

        return message.toString();
    }

    // Name
    public boolean validateName(String name) {
        return name.split(" ").length >= 2;
    }

    // Email
    public boolean validateMailAddress(String mailAddress) {
        if (!mailAddress.contains("@")) return false;

        String[] emailParts = mailAddress.split("@");
        if (emailParts.length != 2) return false;
        if (emailParts[0].isBlank() || emailParts[1].isBlank()) return false;

        if (emailParts[1].contains(".")) {
            String[] domainParts = emailParts[1].split("\\.");
            if (domainParts.length != 2) return false;
            return !domainParts[0].isBlank() && !domainParts[1].isBlank();
        } else {
            return false;
        }
    }

    public boolean mailAddressDoesNotExist(String mailAddress, Map<String, Student> students) {
        return students.get(mailAddress) == null;
    }

    // PostalCode
    public boolean validatePostalCode(String postalCode) {
        if (postalCode.isBlank()) return false;

        postalCode = postalCode.replaceAll(" ", "");
        return postalCode.matches("[1-9][0-9]{3}[a-zA-Z]{2}");
    }

    // Age
    public boolean validateAge(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        int minimumAge = 10; // Minimum age of codecademy is 10 years

        return period.getYears() >= minimumAge;
    }

}
