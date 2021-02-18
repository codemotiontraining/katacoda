package com.example.discovery.domain;

public class Shop {
	private String id;
	private String email;
	private String telephone;
	private String address;

	public Shop() {}
	
	public Shop(String id, String email, String telephone, String address) {
		this.id = id;
		this.email = email;
		this.telephone = telephone;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
