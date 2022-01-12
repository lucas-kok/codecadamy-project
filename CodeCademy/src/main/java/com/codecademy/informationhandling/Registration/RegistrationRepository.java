package com.codecademy.informationhandling.Registration;

import com.codecademy.informationhandling.ContentItem.ContentItem;
import com.codecademy.informationhandling.Databaseconnection.DatabaseConnection;
import com.codecademy.informationhandling.Student.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationRepository {

    private final DatabaseConnection dbCon;

    public RegistrationRepository() {
        dbCon = new DatabaseConnection();
    }

    public void createRegistration(Student student, String courseName, String[] datePieces) throws SQLException {
        String date = datePieces[2] + "/" + datePieces[1] + "/" + datePieces[0];
        String queryCreateRegistration = "INSERT INTO Register values (convert(datetime, '" + date + "', 103), '" + student.getEmail() + "', '" + courseName + "')";
        dbCon.setQuery(queryCreateRegistration);
        ArrayList<Integer> contentItemIDs = new ArrayList<>();
        String querygetAllContentItemsForCourse = "SELECT ContentID FROM ContentItem WHERE CourseName = '" + courseName + "'";
        ResultSet rs = dbCon.getQuery(querygetAllContentItemsForCourse);
        while (rs.next()) {
            contentItemIDs.add(rs.getInt("ContentID"));
        }
        dbCon.CloseResultSet();
        for (Integer id : contentItemIDs) {
            String queryAddProgressItemForRegistration = "INSERT INTO Viewing values ('" + student.getEmail() + "', " + id + ", 0)";
            dbCon.setQuery(queryAddProgressItemForRegistration);
        }
    }

    public ArrayList<Registration> getAllRegistrations() throws SQLException {
        ArrayList<Registration> registrations = new ArrayList<>();
        String queryGetAllRegistrations = "SELECT * FROM Register";
        ResultSet rs = dbCon.getQuery(queryGetAllRegistrations);
        while (rs.next()) {
            registrations.add(new Registration(rs.getInt("RegisterID"), rs.getString("StudentEmail")
                    , rs.getDate("Registerdate").toString(), rs.getString("CourseName")));
        }
        dbCon.CloseResultSet();
        return registrations;
    }

    public void updateRegistration(Registration registration, String[] datePieces) {
        String date = datePieces[2] + "/" + datePieces[1] + "/" + datePieces[0];
        String query = "UPDATE Register SET Registerdate = convert(datetime, '" + date + "', 103) WHERE RegisterID = '" + registration.getRegisterID();
    }

    public void deleteRegistration(Registration registration) {
        String queryDeleteRegistration = "DELETE FROM Register WHERE RegisterID = " + registration.getRegisterID() + " " +
                "                         DELETE FROM Viewing WHERE StudentEmail = '" + registration.getStudentEmail() + "' " +
                "                         AND ContenID IN (SELECT ContenID FROM ContentItem WHERE CourseName = '" + registration.getCourseName() + "')";
    }

    public HashMap<ContentItem, Integer> getProgressForRegistration(Registration registration) throws SQLException {
        HashMap<ContentItem, Integer> progressPerContentItem = new HashMap<>();
        ArrayList<ContentItem> contentItems = new ArrayList<>();
        String queryGetAllContentItems = "SELECT * FROM ContentItem WHERE CourseName = '" + registration.getCourseName() + "'";
        ResultSet rs = dbCon.getQuery(queryGetAllContentItems);
        while (rs.next()) {
            contentItems.add(new ContentItem(rs.getInt("ContentID"), rs.getString("Title"), rs.getString("PublicationDate'")));
        }
        dbCon.CloseResultSet();
        for (ContentItem contentItem : contentItems) {
            String queryGetProgress = "SELECT Progress FROM Viewing WHERE StudentEmail = '" + registration.getStudentEmail() + "' AND ContenID = '" + contentItem.getId() + "'";
            dbCon.getQuery(queryGetProgress);
            progressPerContentItem.put(contentItem, rs.getInt("Progress"));
            dbCon.CloseResultSet();
        }
        return progressPerContentItem;
    }

    public void updateProgress(Registration registration, ContentItem contentItem, int Progress) {
        String queryUpdateProgress = "UPDATE Viewing SET Progress = " + Progress + " WHERE StudentEmail = '" + registration.getStudentEmail() + "' AND ContenID = " + contentItem.getId() + " ";
        dbCon.setQuery(queryUpdateProgress);
    }

}
