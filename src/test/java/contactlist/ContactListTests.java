package contactlist;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ContactListTests extends BaseTest {

    @Test(priority = 1)
    public void TC01_AddNewUser() {
         testEmail = "kanav" + System.currentTimeMillis() + "@gmail.com";
        Response res = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(ContactPayloads.getNewUserPayload(testEmail))
            .post("/users");

        res.then().statusCode(201);
        firstToken = res.jsonPath().getString("token");
    }

    @Test(priority = 2)
    public void TC02_GetUserProfile() {
        RestAssured.given()
            .header("Authorization", "Bearer " + firstToken)
            .get("/users/me")
            .then().statusCode(200)
            .body("email", notNullValue());
    }

    @Test(priority = 3)
    public void TC03_UpdateUser() {
        RestAssured.given()
            .header("Authorization", "Bearer " + firstToken)
            .body(ContactPayloads.getUpdateUserPayload())
            .patch("/users/me")
            .then().statusCode(200);
    }

    @Test(priority = 4)
    public void TC04_LogInUser() {
        Response res = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(ContactPayloads.getLoginPayload(testEmail, testPassword))
            .post("/users/login");

        res.then().statusCode(200);
        secondToken = res.jsonPath().getString("token");
        System.out.println(secondToken);
    }

    @Test(priority = 5)
    public void TC05_AddContact() {
        Response res = RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .header("Content-Type", "application/json")
            .body(ContactPayloads.getAddContactPayload())
            .post("/contacts");
  
        res.then().statusCode(201);
        uid = res.jsonPath().getString("_id");
      
    }


    @Test(priority = 6)
    public void TC06_GetContactList() {
        RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .get("/contacts")
            .then().statusCode(200);
    }

  @Test(priority = 7)
    public void TC07_GetContact() {
        RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .get("/contacts/" + uid)
            .then().statusCode(200)
            .body("firstName", equalTo("John"));
    }

    @Test(priority = 8)
    public void TC08_UpdateFullContact() {
 
    
        RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .body(ContactPayloads.getUpdateFullContactPayload())
            .put("/contacts/" + uid)
            .then().statusCode(200)
            .body("email", equalTo("amiller@fake.com"));
    }

  @Test(priority = 9)
    public void TC09_UpdatePartialContact() {
        RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .body("{ \"firstName\":\"Snow\" }")
            .patch("/contacts/" + uid)
            .then().statusCode(200)
            .body("firstName", equalTo("Snow"));
    }

    @Test(priority = 10)
    public void TC10_LogOutUser() {
        RestAssured.given()
            .header("Authorization", "Bearer " + secondToken)
            .post("/users/logout")
            .then().statusCode(200);
    }
}
