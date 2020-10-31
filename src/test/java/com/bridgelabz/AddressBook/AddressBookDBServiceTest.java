package com.bridgelabz.AddressBook;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AddressBookDBServiceTest
{
    AddressBookSystem addressBookSystem;
    ArrayList<Contacts> contactsList;
    @Before
    public void initialize()
    {
        addressBookSystem = new AddressBookSystem();
    }
    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        List<Contacts> contactList = addressBookSystem.readDataFromDB();
        Assert.assertEquals(4, contactList.size());
    }
}
