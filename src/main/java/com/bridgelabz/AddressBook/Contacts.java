package com.bridgelabz.AddressBook;

import java.util.Objects;

public class Contacts {
	public int id;
	public String firstName;
	public String lastName;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String phoneNo;
	public String email;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Contacts contacts = (Contacts) o;
		return id == contacts.id &&
				Objects.equals(firstName, contacts.firstName) &&
				Objects.equals(lastName, contacts.lastName) &&
				Objects.equals(address, contacts.address) &&
				Objects.equals(city, contacts.city) &&
				Objects.equals(state, contacts.state) &&
				Objects.equals(zip, contacts.zip) &&
				Objects.equals(phoneNo, contacts.phoneNo) &&
				Objects.equals(email, contacts.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, city, state, zip, phoneNo, email);
	}

	public Contacts(String firstName, String lastName, String address, String city, String state, String zip,
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

	public Contacts(int id, String firstName, String lastName, String address, String city, String state, String zip, String phoneNo, String email) {
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

