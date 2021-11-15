package com.example.demo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="student")
public class Student {
 
    private String emailAddress;
    private String name;
    private String purchasedPackage;
 
    public Student() {}
 
    public String getEmailAddress() {
        return emailAddress;
    }
 
    public String getName() {
        return name;
    }
 
    public String getPurchasedPackage() {
        return purchasedPackage;
    }
 
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setPurchasedPackage(String purchasedPackage) {
        this.purchasedPackage = purchasedPackage;
    }
}