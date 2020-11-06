package com.bridgelabz.AddressBook;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static com.bridgelabz.AddressBook.AddressBookSystem.IOType.REST_IO;

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

    private Response addContactToJsonServer(Contacts contact) {
        String contactJson = new Gson().toJson(contact);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(contactJson);
        return request.post("/contacts");
    }

    @Test
    public void givenContactDataInJSONServer_whenRetrieved_shouldMatchTheCount() {
        Contacts[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(3,entries);
    }

    @Test
    public void givenNewContact_whenAdded_shouldMatch201ResponseAndCount(){
        Contacts[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        Contacts contact = new Contacts(4,"Shreya","Mehra","SN Lane","Kolkata","West Bengal","711003","2323232323","shreyamehra@gmail.com", LocalDate.now());
        Response response = addContactToJsonServer(contact);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(201,statusCode);
        contact = new Gson().fromJson(response.asString(),Contacts.class);
        addressBookSystem.addContactToAddressBook(contact,REST_IO);
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(4,entries);
    }

    @Test
    public void givenListOfNewContacts_whenAdded_shouldMatch201ResponseAndCount(){
        Contacts[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        Contacts[] arrayOfNewContacts = {
                new Contacts(0, "Sonal", "Jain", "xyz apartments", "Bengaluru", "Karnataka", "444461", "878786542", "srewd@yahoo.com", LocalDate.now()),
                new Contacts(0, "Priyal", "Tyagi", "qwerty lane", "Indore", "MP", "989861", "1111111111", "something@gmail.com", LocalDate.now())
        };
        for(Contacts contact : arrayOfNewContacts){
            Response response = addContactToJsonServer(contact);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201,statusCode);
            contact = new Gson().fromJson(response.asString(),Contacts.class);
            addressBookSystem.addContactToAddressBook(contact,REST_IO);
        }
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(6,entries);
    }

}
