package tests;

import api.models.*;
import api.spec.Specifications;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.PropertyReader;

import java.io.IOException;
import java.time.Clock;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@Log4j2
public class ApiPositiveTest {

    @Test
    public void createUser() throws IOException {
        String jobOfUser = "leader";
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));
        log.info("user creation");
        CreateAndUpdateData createAndUpdateData;
        createAndUpdateData = CreateAndUpdateData.builder()
                .name(PropertyReader.prop().getProperty("userName"))
                .job(jobOfUser)
                .build();

        SuccessfulCreateAndUpdate responseOfCreatingUser = given()
                .body(createAndUpdateData)
                .when()
                .post(PropertyReader.prop().getProperty("endpoint.create"))
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .body()
                .as(SuccessfulCreateAndUpdate.class);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");
        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(responseOfCreatingUser.getName(), PropertyReader.prop().getProperty("userName"));
        softAssertion.assertEquals(responseOfCreatingUser.getJob(), jobOfUser);
        softAssertion.assertTrue(responseOfCreatingUser.getCreatedAt().contains(currentTime));
        softAssertion.assertTrue(responseOfCreatingUser.getId() > 0);
        softAssertion.assertAll();
    }

    @Test
    public void updateUser() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("updating user 2");
        String updatedJob = "zion resident";
        CreateAndUpdateData createAndUpdateData = CreateAndUpdateData.builder()
                .name(PropertyReader.prop().getProperty("userName"))
                .job(updatedJob)
                .build();

        SuccessfulCreateAndUpdate responseForUpdate = given()
                .body(createAndUpdateData)
                .when()
                .put(PropertyReader.prop().getProperty("endpoint.update"))
                .then().log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(SuccessfulCreateAndUpdate.class);

        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{13})$", "");
        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertTrue(responseForUpdate.getUpdatedAt().contains(currentTime));
        softAssertion.assertTrue(responseForUpdate.getJob() != null);
        softAssertion.assertTrue(responseForUpdate.getName() != null);
        softAssertion.assertAll();
    }

    @Test
    public void deleteUser() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("deleting user 2");
        given()
                .when()
                .delete(PropertyReader.prop().getProperty("endpoint.delete"))
                .then().log().all()
                .assertThat()
                .statusCode(204);

    }

    @Test
    public void successfulRegistration() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData;
        registrationAndLoginData = RegistrationAndLoginData.builder()
                .email(PropertyReader.prop().getProperty("successfulEmail"))
                .password(PropertyReader.prop().getProperty("successfulPassword"))
                .build();

        SuccessfulRegisterAndLogin responseForRegistration = given()
                .body(registrationAndLoginData)
                .when()
                .post(PropertyReader.prop().getProperty("endpoint.registration"))
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(SuccessfulRegisterAndLogin.class);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertTrue(responseForRegistration.getId() > 0);
        softAssertion.assertTrue(responseForRegistration.getToken() != null);

        log.info("check if the required user is in the list");
        List<User> users = given()
                .when()
                .get(PropertyReader.prop().getProperty("endpoint.listUsers"))
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", User.class);
        softAssertion.assertTrue(users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase("eve.holt@reqres.in")));
        softAssertion.assertAll();
    }

    @Test
    public void successfulLogin() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("user logs in");
        RegistrationAndLoginData registrationAndLoginData;
        registrationAndLoginData = RegistrationAndLoginData.builder()
                .email(PropertyReader.prop().getProperty("successfulEmail"))
                .password("cityslicka")
                .build();

        SuccessfulRegisterAndLogin responseForLogin = given()
                .body(registrationAndLoginData)
                .when()
                .post(PropertyReader.prop().getProperty("endpoint.login"))
                .then().log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(SuccessfulRegisterAndLogin.class);

        assertTrue(responseForLogin.getToken() != null);
    }

}
