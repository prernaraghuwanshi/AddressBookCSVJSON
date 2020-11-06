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
        String query = String.format("select * from contact where date_added between '%s' and '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getContactDataUsingDB(query);
    }

    public Map<String, Integer> getContactInCity() {
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
            e.printStackTrace();
        }
        return contactMap;
    }

    public Contacts addContact(String firstName, String lastName, String address, String city, String state, String zip, String number, String email, LocalDate registeredDate) {
        int contact_id = -1;
        Connection connection = null;
        Contacts contact = null;
        connection = this.getConnection();

        try (Statement statement = connection.createStatement();) {
            String sql = String.format("insert into contact(first_name, last_name, address,city, state,zip," +
                            "phone_number,email, date_added) values ('%s','%s','%s','%s','%s','%s','%s','%s','%s')"
                    , firstName, lastName, address, city, state, zip, number, email, Date.valueOf(registeredDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    contact_id = resultSet.getInt(1);
                    contact = new Contacts(contact_id, firstName, lastName, address, city, state, zip, number, email, registeredDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }

    public Contacts addContactToDB(String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded, int addressBookId, String[] type) {
        Connection[] connection = new Connection[1];
        final Contacts[] contact = {null};
        connection[0] = this.getConnection();
        try {
            connection[0].setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int contact_id = addToContact(connection[0], firstName, lastName, address, city, state, zip, phone, email, dateAdded);
        final boolean[] flag = {false};
        Runnable task = () -> {
            boolean result = addToContactListing(connection[0], contact_id,addressBookId,type);
            if (result) {
                contact[0]  = new Contacts(contact_id, firstName, lastName, address, city, state, zip, phone, email, dateAdded,addressBookId,type);
            }
            flag[0] = true;
        };
        Thread thread = new Thread(task);
        thread.start();
        if (flag[0] == false) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            connection[0].commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection[0].close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return contact[0];
    }

    private int addToContact(Connection connection, String firstName, String lastName, String address, String city, String state, String zip, String phone, String email, LocalDate dateAdded) {
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
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private boolean addToContactListing(Connection connection, int contact_id, int addressBookId, String[] type) {
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
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
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
