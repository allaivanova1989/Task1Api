package tests;

import api.models.RegistrationAndLoginData;
import api.models.UnSuccessfulRegisterAndLogin;
import api.spec.Specifications;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import utils.PropertyReader;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

@Log4j2
public class ApiNegativeTest {

    @Test
    public void unsuccessfulRegistration() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData;
        registrationAndLoginData = RegistrationAndLoginData.builder()
                .email("sydney@fife")
                .build();

        UnSuccessfulRegisterAndLogin negativeResponseForRegistration = given()
                .body(registrationAndLoginData)
                .when()
                .post(PropertyReader.prop().getProperty("endpoint.registration"))
                .then().log().all()
                .statusCode(400)
                .extract()
                .body()
                .as(UnSuccessfulRegisterAndLogin.class);

        assertEquals(negativeResponseForRegistration.getError(), PropertyReader.prop().getProperty("errorMessage"), "ErrorMessage is incorrect");
    }

    @Test
    public void unsuccessfulLogin() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("user log in");
        RegistrationAndLoginData registrationAndLoginData;
        registrationAndLoginData = RegistrationAndLoginData.builder()
                .email("sydney@fife")
                .build();

        UnSuccessfulRegisterAndLogin negativeResponseForLogin = given()
                .body(registrationAndLoginData)
                .when()
                .post(PropertyReader.prop().getProperty("endpoint.login"))
                .then().log().all()
                .statusCode(400)
                .extract()
                .body()
                .as(UnSuccessfulRegisterAndLogin.class);

        assertEquals(negativeResponseForLogin.getError(), PropertyReader.prop().getProperty("errorMessage"), "ErrorMessage is incorrect");
    }

    @Test
    public void userAbsenceCheck() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec(PropertyReader.prop().getProperty("url")));

        log.info("user absence check");
        given()
                .when()
                .get(PropertyReader.prop().getProperty("endpoint.singleUserNotFound"))
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}
