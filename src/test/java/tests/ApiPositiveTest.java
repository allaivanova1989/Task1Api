package tests;

import api.models.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.time.Clock;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static utils.PropertyReader.getProperty;

@Log4j2
public class ApiPositiveTest extends BaseTest {

    @Test
    public void createUser() {
        String jobOfUser = "leader";
        log.info("user creation");
        CreateAndUpdateData createAndUpdateData =
                new CreateAndUpdateData(getProperty("userName"), jobOfUser);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");
        given()
                .body(createAndUpdateData)
                .when()
                .post(getProperty("create"))
                .then()
                .assertThat()
                .statusCode(201)
                .body("name", is("morpheus"),
                        "job", is("leader"),
                        "createdAt", containsString(currentTime),
                        "id", notNullValue());

    }

    @Test
    public void updateUser() {
        log.info("updating user 2");
        String updatedJob = "zion resident";
        CreateAndUpdateData createAndUpdateData =
                new CreateAndUpdateData(getProperty("userName"), updatedJob);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");

        given()
                .body(createAndUpdateData)
                .when()
                .put(getProperty("user2"))
                .then()
                .statusCode(200)
                .body("name", notNullValue(),
                        "job", notNullValue(),
                        "updatedAt", containsString(currentTime));

    }

    @Test
    public void deleteUser() {
        log.info("Saving of user2");
        String userEmail = given()
                .when()
                .get(getProperty("user2"))
                .then()
                .statusCode(200)
                .extract()
                .path("data.email");

        log.info("deleting user 2");
        given()
                .when()
                .delete(getProperty("user2"))
                .then()
                .assertThat()
                .statusCode(204);


        log.info("check if the deleted user2 is not in the list");
        given()
                .when()
                .get(getProperty("listUsers"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.email", allOf(not(hasItem(userEmail))));


    }

    @Test
    public void successfulRegistration() {
        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData =
                new RegistrationAndLoginData(getProperty("successfulEmail"), getProperty("successfulPassword"));

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty("registration"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue(),
                        "id", notNullValue());


        log.info("check if the required user is in the list");
        given()
                .when()
                .get(getProperty("listUsers"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.email", anyOf(hasItem("eve.holt@reqres.in")));


    }

    @Test
    public void successfulLogin() {
        log.info("user logs in");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData(getProperty("successfulEmail"), "cityslicka");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty("login"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue());

    }

}
