package com.CustomerApi.Customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;

@JsonIgnoreProperties
public class Customer {
    @JsonProperty("uuid")
    public String uuid;
    @NonNull
    @JsonProperty("first_name")
    public String firstName;
    @NonNull
    @JsonProperty("last_name")
    public String lastName;
    @JsonProperty("street")
    public String street;
    @JsonProperty("address")
    public String address;
    @JsonProperty("city")
    public String city;
    @JsonProperty("state")
    public String state;
    @JsonProperty("email")
    public String email;
    @JsonProperty("phone")
    public String phone;

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User [uuid=" + uuid + " ,firstName=" + firstName + ", lastName=" + lastName + ", street=" + street
                + ", address=" + address
                + ", city=" + city + ", state=" + state + ", email=" + email + ", phone=" + phone + "]";
    }

    public String getUuid() {
        return uuid;
    }

}