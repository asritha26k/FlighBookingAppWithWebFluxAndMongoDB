package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("address")
public class Address {

    @Id
    private String addressId;

    private Integer houseNo;

    private String city;
    private String state;
    private String country;

    private String passengerId; //just reference to passenger object id


   // public Integer getHouseNo() { return houseNo; }
    public void setHouseNo(Integer houseNo) { this.houseNo = houseNo; }

   // public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	//public String getState() { return state; }
    public void setState(String state) { this.state = state; }

  //  public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

  //  public String getPassengerId() { return passengerId; }
    public void setPassengerId(String string) { this.passengerId = string; }
}
