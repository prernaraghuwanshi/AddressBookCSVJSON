package com.bridgelabz.AddressBook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    String query = "Select * from contact;";
    private static PreparedStatement contactDataStatement;
    private static AddressBookDBService addressBookDBService;

    public AddressBookDBService() {
    }

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private Connection getConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "root";
        Connection con = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!" + con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public List<Contacts> readData() throws SQLException {
        return this.getContactDataUsingDB(query);
    }

    public int updateContactData(String name, String newAddress) {
        String query = "update contact set address= ? where first_name= ?";
        try (Connection connection = this.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newAddress);
            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Contacts> getContactInDateRange(LocalDate startDate, LocalDate endDate) {
        String query =String.format("select * from contact where date_added between '%s' and '%s';",Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getContactDataUsingDB(query);
    }

    public List<Contacts> getContactInCity(String city) {
        String query = String.format("select * from contact where city = '%s';",city);
        return this.getContactDataUsingDB(query);
    }

    private List<Contacts> getContactDataUsingDB(String query) {
        List<Contacts> contactDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            contactDataList = this.getContactData(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactDataList;
    }

    public List<Contacts> getContactData(String name) {
        List<Contacts> contactDataList = null;
        if (contactDataStatement == null)
            this.prepareStatementForContactData();
        try {
            contactDataStatement.setString(1, name);
            ResultSet resultSet = contactDataStatement.executeQuery();
            contactDataList = this.getContactData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactDataList;
    }

    private List<Contacts> getContactData(ResultSet resultSet) {
        List<Contacts> contactsList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("contact_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String phoneNumber = resultSet.getString("phone_number");
                String email = resultSet.getString("email");
                contactsList.add(new Contacts(id, firstName, lastName, address, city, state, zip, phoneNumber, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    private void prepareStatementForContactData() {
        try {
            Connection connection = this.getConnection();
            String query = "select * from contact where first_name = ?";
            contactDataStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
