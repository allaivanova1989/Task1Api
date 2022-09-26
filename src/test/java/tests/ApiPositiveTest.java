package tests;

import api.models.*;
import api.spec.Specifications;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Clock;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;
import static utils.Constants.PropertyName.*;
import static utils.PropertyReader.getProperty;

@Log4j2
public class ApiPositiveTest {

    @Test
    public void createUser() throws IOException {
        String jobOfUser = "leader";
        Specifications.installSpecification(Specifications.requestSpec());
        log.info("user creation");
        CreateAndUpdateData createAndUpdateData =
                new CreateAndUpdateData(getProperty(USER_NAME), jobOfUser);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");
        given()
                .body(createAndUpdateData)
                .when()
                .post(getProperty(CREATE))
                .then()
                .assertThat()
                .statusCode(201)
                .body("name", is("morpheus"),
                        "job", is("leader"),
                        "createdAt", containsString(currentTime),
                        "id", notNullValue());


    }

    @Test
    public void updateUser() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("updating user 2");
        String updatedJob = "zion resident";
        CreateAndUpdateData createAndUpdateData =
                new CreateAndUpdateData(getProperty(USER_NAME), updatedJob);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");

        given()
                .body(createAndUpdateData)
                .when()
                .put(getProperty(UPDATE))
                .then()
                .statusCode(200)
                .body("name", notNullValue(),
                        "job", notNullValue(),
                        "updatedAt", containsString(currentTime));


    }

    @Test
    public void deleteUser() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("Saving of user2");
        SingleUser user2 = given()
                .when()
                .get(getProperty(DELETE))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(SingleUser.class);

        String emailOfDeletedUser = user2.getData().getEmail();

        log.info("deleting user 2");
        given()
                .when()
                .delete(getProperty(DELETE))
                .then()
                .assertThat()
                .statusCode(204);


        log.info("check if the deleted user2 is not in the list");
        List<User> users = given()
                .when()
                .get(getProperty(LIST_USER))
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", User.class);
        assertFalse(users.stream().anyMatch(anyUser -> anyUser.getEmail().equalsIgnoreCase(emailOfDeletedUser)));

    }

    @Test
    public void successfulRegistration() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData =
                new RegistrationAndLoginData(getProperty(SUCCESSFUL_EMAIL), getProperty(SUCCESSFUL_PASSWORD));

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty(REGISTER))
                .then()
                .assertThat()
                .statusCode(200)

                .body("token", notNullValue(),
                        "id", notNullValue());


        log.info("check if the required user is in the list");
        List<User> users = given()
                .when()
                .get(getProperty(LIST_USER))
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", User.class);
        assertTrue(users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase("eve.holt@reqres.in")));

    }

    @Test
    public void successfulLogin() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("user logs in");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData(getProperty(SUCCESSFUL_EMAIL), "cityslicka");


        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty(LOGIN))
                .then()
                .statusCode(200)
                .body("token", notNullValue());

    }

}
