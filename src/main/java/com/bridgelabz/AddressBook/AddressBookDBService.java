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
        String password = "M4A4T!Hs";
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
                contactsList.add(new Contacts(id, firstName,lastName, address,city,state,zip,phoneNumber,email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    private void prepareStatementForContactData() {
        try {
            Connection connection = this.getConnection();
            String query = "select * from contact where name = ?";
            contactDataStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
