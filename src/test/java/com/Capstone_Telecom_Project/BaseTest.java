package com.Capstone_Telecom_Project;



import io.restassured.RestAssured;

public class BaseTest {
    protected static final String BASE_URL = "https://thinking-tester-contact-list.herokuapp.com";
    protected static String firstToken;
    protected static String secondToken;
    protected static String uid;
    protected static String userid;
    
    protected static String testEmail;
    protected static final String testPassword = "myPassword";

    static {
        RestAssured.baseURI = BASE_URL;
    }
}
