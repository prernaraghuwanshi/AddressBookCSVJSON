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

    private Contact[] getContactList() {
        Response response = RestAssured.get("/contacts");
        System.out.println("Contact Data in JSON Server: \n" + response.asString());
        Contact[] arrayOfContacts = new Gson().fromJson(response.asString(), Contact[].class);
        return arrayOfContacts;
    }

    private Response addContactToJsonServer(Contact contact) {
        String contactJson = new Gson().toJson(contact);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(contactJson);
        return request.post("/contacts");
    }

    @Test
    public void givenContactDataInJSONServer_whenRetrieved_shouldMatchTheCount() {
        Contact[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(3,entries);
    }

    @Test
    public void givenNewContact_whenAdded_shouldMatch201ResponseAndCount(){
        Contact[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        Contact contact = new Contact(4,"Shreya","Mehra","SN Lane","Kolkata","West Bengal","711003","2323232323","shreyamehra@gmail.com", LocalDate.now());
        Response response = addContactToJsonServer(contact);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(201,statusCode);
        contact = new Gson().fromJson(response.asString(), Contact.class);
        addressBookSystem.addContactToAddressBook(contact,REST_IO);
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(4,entries);
    }

    @Test
    public void givenListOfNewContacts_whenAdded_shouldMatch201ResponseAndCount(){
        Contact[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        Contact[] arrayOfNewContacts = {
                new Contact(0, "Sonal", "Jain", "xyz apartments", "Bengaluru", "Karnataka", "444461", "878786542", "srewd@yahoo.com", LocalDate.now()),
                new Contact(0, "Priyal", "Tyagi", "qwerty lane", "Indore", "MP", "989861", "1111111111", "something@gmail.com", LocalDate.now())
        };
        for(Contact contact : arrayOfNewContacts){
            Response response = addContactToJsonServer(contact);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201,statusCode);
            contact = new Gson().fromJson(response.asString(), Contact.class);
            addressBookSystem.addContactToAddressBook(contact,REST_IO);
        }
        long entries = addressBookSystem.countEntries();
        Assert.assertEquals(6,entries);
    }

    @Test
    public void givenNewAddress_whenUpdated_shouldMatch200Response(){
        Contact[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        addressBookSystem.updateAddress("Priyal","RK Apartments",REST_IO);
        Contact contact = addressBookSystem.getContactData("Priyal");

        String contactJson = new Gson().toJson(contact);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(contactJson);
        Response response = request.put("/contacts/"+contact.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void givenContactToDelete_whenDeleted_shouldMatch200ResponseAndCount(){
        Contact[] arrayOfContacts = getContactList();
        addressBookSystem = new AddressBookSystem(Arrays.asList(arrayOfContacts));
        Contact contact = addressBookSystem.getContactData("Sonal");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        Response response = request.delete("/contacts/"+contact.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200,statusCode);

        int entries = addressBookSystem.deleteContact(contact.firstName);
        Assert.assertEquals(5,entries);
    }

}
