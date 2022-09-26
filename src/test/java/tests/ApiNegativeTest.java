package tests;

import api.models.RegistrationAndLoginData;
import api.spec.Specifications;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.PropertyName.*;
import static utils.PropertyReader.getProperty;

@Log4j2
public class ApiNegativeTest {

    @Test
    public void unsuccessfulRegistration() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData("sydney@fife", "");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty(REGISTER))
                .then()
                .statusCode(400)
                .body("error", equalTo(getProperty(ERROR_MESSAGE)));

    }

    @Test
    public void unsuccessfulLogin() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("user log in");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData("sydney@fife", "");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getProperty(LOGIN))
                .then()
                .statusCode(400)
                .body("error", equalTo(getProperty(ERROR_MESSAGE)));

    }

    @Test
    public void userAbsenceCheck() throws IOException {
        Specifications.installSpecification(Specifications.requestSpec());

        log.info("user absence check");
        given()
                .when()
                .get(getProperty(SINGLE_USER))
                .then()
                .assertThat()
                .statusCode(404);
    }
}
