package com.bridgelabz.AddressBook;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AddressBookRestAssuredTest {
    AddressBookSystem addressBookSystem;

    @Before
    public void initialize() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    private Contacts[] getContactList() {
        Response response = RestAssured.get("/contacts");
        System.out.println("Contact Data in JSON Server: \n" + response.asString());
        Contacts[] arrayOfContacts = new Gson().fromJson(response.asString(), Contacts[].class);
        return arrayOfContacts;
    }

    @Test
    public void givenContactDataInJSONServer_whenRetrieved_shouldMatchTheCount() {
        Contacts[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(3,entries);
    }

}
