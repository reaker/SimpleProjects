package ShelterManager;

import java.sql.*;
import javax.swing.*;

class SQL {

    static void insert(Statement statement, String animalName, String breed, char sex, int health) throws SQLException {
        statement.executeUpdate("Insert into ShelterManager2 values(null,'"+animalName+"','"+breed+"','"+sex+"'," +health+")");
    }

    static void delete(Statement statement, int ID) throws SQLException{
        statement.executeUpdate("Delete from ShelterManager2 where ID="+ID+";");
    }

    static int count(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("select * from ShelterManager2");
        int count = 0;
        while (rs.next()) count++;
        return count;
    }

    static void status(Statement statement, Connection con) throws SQLException{
        // new TableFromMySqlDatabase(con);
        TableFromMySqlDatabase frame = new TableFromMySqlDatabase(con);
        frame.setTitle("Manager Schroniska - baza danych");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
