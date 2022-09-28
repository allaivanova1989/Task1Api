package tests;

import api.models.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.time.Clock;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;
import static utils.PropertyReader.getPropertyByName;

@Log4j2
public class ApiPositiveTest extends BaseTest {

    @Test
    public void createUser() {
        String jobOfUser = "leader";
        log.info("user creation");
        CreateAndUpdateData createAndUpdateData =
                new CreateAndUpdateData(getPropertyByName("userName"), jobOfUser);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");
        given()
                .body(createAndUpdateData)
                .when()
                .post(getPropertyByName("create"))
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
                new CreateAndUpdateData(getPropertyByName("userName"), updatedJob);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");

        given()
                .body(createAndUpdateData)
                .when()
                .put(getPropertyByName("user2"))
                .then()
                .statusCode(200)
                .body("name", notNullValue(),
                        "job", notNullValue(),
                        "updatedAt", containsString(currentTime));

    }

    @Test
    public void deleteUser() {
        log.info("Saving of user2");
        SingleUser user2 = given()
                .when()
                .get(getPropertyByName("user2"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(SingleUser.class);

        String emailOfDeletedUser = user2.getData().getEmail();

        log.info("deleting user 2");
        given()
                .when()
                .delete(getPropertyByName("user2"))
                .then()
                .assertThat()
                .statusCode(204);


        log.info("check if the deleted user2 is not in the list");
        List<User> users = given()
                .when()
                .get(getPropertyByName("listUsers"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", User.class);
        assertFalse(users.stream().anyMatch(anyUser -> anyUser.getEmail().equalsIgnoreCase(emailOfDeletedUser)));

    }

    @Test
    public void successfulRegistration() {
        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData =
                new RegistrationAndLoginData(getPropertyByName("successfulEmail"), getPropertyByName("successfulPassword"));

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getPropertyByName("registration"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue(),
                        "id", notNullValue());


        log.info("check if the required user is in the list");
        List<User> users = given()
                .when()
                .get(getPropertyByName("listUsers"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", User.class);
        assertTrue(users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase("eve.holt@reqres.in")));

    }

    @Test
    public void successfulLogin() {
        log.info("user logs in");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData(getPropertyByName("successfulEmail"), "cityslicka");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getPropertyByName("login"))
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue());

    }

}
