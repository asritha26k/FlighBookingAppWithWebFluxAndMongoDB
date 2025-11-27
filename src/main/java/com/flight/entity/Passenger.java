package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;




@Document("passenger")
public class Passenger {

    @Id
    private String passengerId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Long phoneNo;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String emailId;
    
    
    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }

   // public String getName() { return name; }
    public void setName(String name) { this.name = name; }

   // public Long getPhoneNo() { return phoneNo; }
    public void setPhoneNo(Long phoneNo) { this.phoneNo = phoneNo; }

   // public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
}
