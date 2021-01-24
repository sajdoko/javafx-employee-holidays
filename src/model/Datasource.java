package model;

import java.sql.*;

public class Datasource {

    public static final String CONNECTION_STRING = "jdbc:mariadb://localhost:3306/employee_holidays?user=root&password=";


    private Connection conn;
    private static final Datasource instance = new Datasource();
    private Datasource() {

    }
    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public Employee checkEmployee(String username) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM `employees` WHERE `username` = ?");
        preparedStatement.setString(1, username);
        ResultSet results = preparedStatement.executeQuery();

        Employee employee = new Employee();
        if (results.next()) {

            employee.setId(results.getInt("id"));
            employee.setFullName(results.getString("name") + " " + results.getString("surname"));
            employee.setUsername(results.getString("username"));
            employee.setHolidays(results.getInt("holidays"));

        }

        return employee;
    }

    public boolean decreaseHolidays(int nrDays, String username) {
        String sql = "UPDATE `employees` SET `holidays` = (`holidays` - ?)  WHERE `username` = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, nrDays);
            statement.setString(2, username);
            int rows = statement.executeUpdate();
            System.out.println(rows + " record(s) updated.");
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }
}















