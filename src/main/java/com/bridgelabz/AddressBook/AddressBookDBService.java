package com.bridgelabz.AddressBook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
    String query = "Select * from contact;";
    private static PreparedStatement contactDataStatement;
    private static AddressBookDBService addressBookDBService;

    // Constructor
    public AddressBookDBService() {
    }

    // Get instance of AddressBookService
    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    // Get connection to DB
    private Connection getConnection() throws AddressBookException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "M4A4T!Hs";
        Connection con = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!" + con);
        } catch (SQLException e) {
            throw new AddressBookException("Connection not established",AddressBookException.ExceptionType.CONNECTION_ISSUE);
        }
        return con;
    }

    //  Show data in DB
    public List<Contact> readData() throws AddressBookException {
        return this.getContactDataUsingDB(query);
    }

    // Update address of a contact
    public int updateContactData(String name, String newAddress) throws AddressBookException {
        String query = "update contact set address= ? where first_name= ?";
        try (Connection connection = this.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newAddress);
            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
    }

    // All contacts added in a date range
    public List<Contact> getContactInDateRange(LocalDate startDate, LocalDate endDate) throws AddressBookException {
        String query = String.format("select * from contact where date_added between '%s' and '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getContactDataUsingDB(query);
    }

    // All contacts in a city
    public Map<String, Integer> getContactInCity() throws AddressBookException {
        Map<String, Integer> contactMap = new HashMap<String, Integer>();
        String query = String.format("select city,count(contact_id) as count from contact group by city;");
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int count = result.getInt("count");
                String city = result.getString("city");
                contactMap.put(city, count);
            }
        } catch (SQLException e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
        return contactMap;
    }

    // Add to Contacts table
    public Contact addContact(String firstName, String lastName, String address, String city, String state, String zip, String number, String email, LocalDate registeredDate) throws AddressBookException {
        Connection connection = null;
        Contact contact = null;
        connection = this.getConnection();
        int contact_id = this.addToContact(connection, firstName, lastName, address, city, state, zip, number, email, registeredDate);
        contact = new Contact(contact_id, firstName, lastName, address, city, state, zip, number, email, registeredDate);
        return contact;
    }

    // Add to Contacts and Contact_listing table
    public Contact addContactToDB(String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded, int addressBookId, String[] type) throws AddressBookException {
        Connection[] connection = new Connection[1];
        final Contact[] contact = {null};
        connection[0] = this.getConnection();
        try {
            connection[0].setAutoCommit(false);
        } catch (SQLException throwables) {
            throw new AddressBookException("Auto commit error",AddressBookException.ExceptionType.COMMIT_ISSUE);
        }
        int contact_id = addToContact(connection[0], firstName, lastName, address, city, state, zip, phone, email, dateAdded);
        final boolean[] flag = {false};
        Runnable task = () -> {
            boolean result = false;
            try {
                result = addToContactListing(connection[0], contact_id, addressBookId, type);
            } catch (AddressBookException e) {
            }
            if (result) {
                contact[0] = new Contact(contact_id, firstName, lastName, address, city, state, zip, phone, email, dateAdded, addressBookId, type);
            }
            flag[0] = true;
        };
        Thread thread = new Thread(task);
        thread.start();
        if (flag[0] == false) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new AddressBookException("Thread sleep error",AddressBookException.ExceptionType.THREAD_ISSUE);
            }
        }
        try {
            connection[0].commit();
        } catch (SQLException throwables) {
            throw new AddressBookException("Commit error",AddressBookException.ExceptionType.COMMIT_ISSUE);
        } finally {
            if (connection != null) {
                try {
                    connection[0].close();
                } catch (SQLException throwables) {
                    throw new AddressBookException("Connection closing error",AddressBookException.ExceptionType.CONNECTION_ISSUE);
                }
            }
        }
        return contact[0];
    }

    // Add to Contact table and give contact_id
    private int addToContact(Connection connection, String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded) throws AddressBookException {
        int contact_id = -1;
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into contact(first_name,last_name,address,city,state,zip,phone_number,email,date_added)" +
                    "values('%s','%s','%s','%s','%s','%s','%s','%s','%s')", firstName, lastName, address, city, state, zip, phone, email, dateAdded);
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    contact_id = resultSet.getInt(1);
            }
            return contact_id;
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new AddressBookException("Unable to rollback",AddressBookException.ExceptionType.ROLLBACK_ISSUE);
            }
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
    }

    // Add to contact_listing table
    private boolean addToContactListing(Connection connection, int contact_id, int addressBookId, String[] type) throws AddressBookException {
        try (Statement statement = connection.createStatement()) {
            for (int i = 0; i < type.length; i++) {
                String sql = String.format("insert into contact_listing values ('%s','%s','%s')", addressBookId, contact_id, type[i]);
                int rowAffected = statement.executeUpdate(sql);
                if (rowAffected != 1) {
                    return false;
                }
            }
            return true;
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new AddressBookException("Unable to rollback",AddressBookException.ExceptionType.ROLLBACK_ISSUE);
            }
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
    }

    // Execute query and store contact data in list
    private List<Contact> getContactDataUsingDB(String query) throws AddressBookException {
        List<Contact> contactDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            contactDataList = this.getContactData(result);
        } catch (SQLException e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
        return contactDataList;
    }

    // Give contact details by name
    public List<Contact> getContactData(String name) throws AddressBookException {
        List<Contact> contactDataList = null;
        if (contactDataStatement == null)
            this.prepareStatementForContactData();
        try {
            contactDataStatement.setString(1, name);
            ResultSet resultSet = contactDataStatement.executeQuery();
            contactDataList = this.getContactData(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
        return contactDataList;
    }

    // Add to contactsList given resultSet
    private List<Contact> getContactData(ResultSet resultSet) throws AddressBookException {
        List<Contact> contactList = new ArrayList<>();
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
                contactList.add(new Contact(id, firstName, lastName, address, city, state, zip, phoneNumber, email));
            }
        } catch (SQLException e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
        return contactList;
    }

    // Prepared statement execution
    private void prepareStatementForContactData() throws AddressBookException {
        try {
            Connection connection = this.getConnection();
            String query = "select * from contact where first_name = ?";
            contactDataStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new AddressBookException("Unable to execute query",AddressBookException.ExceptionType.QUERY_ISSUE);
        }
    }
}