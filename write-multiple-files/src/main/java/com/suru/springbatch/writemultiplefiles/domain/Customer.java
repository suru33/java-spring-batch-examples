package com.suru.springbatch.writemultiplefiles.domain;

public class Customer {
	private int id;
	private String firstName;
	private String lastName;
	private String random;

	public Customer() {
	}

	public Customer(int id, String firstName, String lastName, String random) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.random = random;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", random=" + random
				+ "]";
	}
}