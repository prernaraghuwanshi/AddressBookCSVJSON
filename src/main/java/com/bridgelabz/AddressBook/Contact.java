package com.bridgelabz.AddressBook;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
	public int id;
	public String firstName;
	public String lastName;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String phoneNo;
	public String email;
	public LocalDate dateAdded;
	public int addressBookId;
	public String[] type;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Contact contact = (Contact) o;
		return id == contact.id &&
				Objects.equals(firstName, contact.firstName) &&
				Objects.equals(lastName, contact.lastName) &&
				Objects.equals(address, contact.address) &&
				Objects.equals(city, contact.city) &&
				Objects.equals(state, contact.state) &&
				Objects.equals(zip, contact.zip) &&
				Objects.equals(phoneNo, contact.phoneNo) &&
				Objects.equals(email, contact.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, city, state, zip, phoneNo, email);
	}

	public Contact(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNo, String email, LocalDate dateAdded, int addressBookId, String[] type) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
		this.dateAdded = dateAdded;
		this.addressBookId = addressBookId;
		this.type = type;
	}

	public Contact(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNo, String email, LocalDate dateAdded) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
		this.dateAdded = dateAdded;
	}

	public Contact(String firstName, String lastName, String address, String city, String state, String zip,
				   String phoneNo, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
	}

	public Contact(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNo, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNo = phoneNo;
		this.email = email;
	}

	@Override
	public String toString() {
		return "FirstName: " + firstName + " LastName: " + lastName + " Address: " + address + " City: " + city
				+ " State: " + state + " ZIP: " + zip + " PhoneNo: " + phoneNo + " Email: " + email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}