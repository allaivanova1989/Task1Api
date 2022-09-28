package tests;

import api.models.RegistrationAndLoginData;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utils.PropertyReader.getPropertyByName;

@Log4j2
public class ApiNegativeTest extends BaseTest {

    @Test
    public void unsuccessfulRegistration() {
        log.info("user registration");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData("sydney@fife", "");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getPropertyByName("registration"))
                .then()
                .statusCode(400)
                .body("error", equalTo(getPropertyByName("errorMessage")));

    }

    @Test
    public void unsuccessfulLogin() {
        log.info("user log in");
        RegistrationAndLoginData registrationAndLoginData = new RegistrationAndLoginData("sydney@fife", "");

        given()
                .body(registrationAndLoginData)
                .when()
                .post(getPropertyByName("login"))
                .then()
                .statusCode(400)
                .body("error", equalTo(getPropertyByName("errorMessage")));

    }

    @Test
    public void userAbsenceCheck() {
        log.info("user absence check");
        given()
                .when()
                .get(getPropertyByName("singleUserNotFound"))
                .then()
                .assertThat()
                .statusCode(404);
    }
}
