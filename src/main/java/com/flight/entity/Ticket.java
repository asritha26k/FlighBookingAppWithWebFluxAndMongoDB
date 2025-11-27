package com.flight.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Document("ticket")
public class Ticket {

    @Id
    private String ticketId;

    @NotBlank
    private String pnr;

    @NotBlank
    private String seatNo;

    @NotNull
    private Status status;

    private String flightId;

    private String passengerId;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String string) {
        this.flightId = string;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String string) {
        this.passengerId = string;
    }

    
}
