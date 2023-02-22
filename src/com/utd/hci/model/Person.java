package com.utd.hci.model;

import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class for a Person.
 *
 * @author Shalini Gautam
 */
public class Person {

    private StringProperty firstName;
    private String middleInitial;
    private String lastName;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zipcode;
    private String gender;
    private StringProperty phoneNumber;
    private String email;
    private boolean proofOfPurchaseAttached;
    private LocalDate dateReceived ;
    private String formStartedAt;
    private String formSavedAt;
    private int backspaceCounter;

    public StringProperty getFirstName() {
        return firstName;
    }

    public void setFirstName(StringProperty firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public StringProperty getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringProperty phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isProofOfPurchaseAttached() {
        return proofOfPurchaseAttached;
    }

    public void setProofOfPurchaseAttached(boolean proofOfPurchaseAttached) {
        this.proofOfPurchaseAttached = proofOfPurchaseAttached;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getFormStartedAt() {
        return formStartedAt;
    }

    public void setFormStartedAt(String formStartedAt) {
        this.formStartedAt = formStartedAt;
    }

    public String getFormSavedAt() {
        return formSavedAt;
    }

    public void setFormSavedAt(String formSavedAt) {
        this.formSavedAt = formSavedAt;
    }

    public int getBackspaceCounter() {
        return backspaceCounter;
    }

    public void setBackspaceCounter(int backspaceCounter) {
        this.backspaceCounter = backspaceCounter;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName=" + firstName.getValue() +
                "\t middleInitial='" + middleInitial + '\'' +
                "\t lastName='" + lastName + '\'' +
                "\t line1='" + line1 + '\'' +
                "\t line2='" + line2 + '\'' +
                "\t city='" + city + '\'' +
                "\t state='" + state + '\'' +
                "\t zipcode=" + zipcode +
                "\t gender='" + gender + '\'' +
                "\t phoneNumber=" + phoneNumber.getValue() +
                "\t email='" + email + '\'' +
                "\t proofOfPurchaseAttached=" + proofOfPurchaseAttached +
                "\t dateReceived=" + dateReceived +
                "\t formStartedAt=" + formStartedAt +
                "\t formSavedAt=" + formSavedAt +
                "\t backspaceCounter=" + backspaceCounter +
                '}';
    }
}