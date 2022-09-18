package tests;

import api.models.RegistrationAndLogin;
import api.models.Response;
import api.spec.BasicDetails;
import api.spec.Specifications;
import io.restassured.mapper.ObjectMapperType;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;


@Log4j2
public class apiTestNegative {
    private RegistrationAndLogin registration;

    @Test
    public void unsuccessfulRegistration() {

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL));

        log.info("user registration");
        registration = RegistrationAndLogin.builder()
                .email("sydney@fife")
                .build();

             Response negativeResponseForRegistration = given()
                .body(registration)
                .when()
                .post("/api/register")
                .then().log().all()
                .statusCode(400)
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        SoftAssert softAssertion = new SoftAssert();
        if (negativeResponseForRegistration.getError() != null) {
            softAssertion.assertEquals(negativeResponseForRegistration.getError(), BasicDetails.errorMessage, "ErrorMessage is incorrect");
        } else
            softAssertion.assertTrue(false, "It is possible to register without Password");
        softAssertion.assertAll();
    }

    @Test
    public void unsuccessfulLogin() {

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL));

        log.info("user registration");
        registration = RegistrationAndLogin.builder()
                .email("sydney@fife")
                .build();

        Response negativeResponseForLogin = given()
                .body(registration)
                .when()
                .post("/api/login")
                .then().log().all()
                .statusCode(400)
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        SoftAssert softAssertion = new SoftAssert();
        if (negativeResponseForLogin.getError() != null) {
            softAssertion.assertEquals(negativeResponseForLogin.getError(), BasicDetails.errorMessage, "ErrorMessage is incorrect");
        } else
            softAssertion.assertTrue(false, "It is possible to login without password");
        softAssertion.assertAll();
    }

    @Test
    public void userAbsenceCheck(){

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL));

        log.info("user absence check");
        given()
                .when()
                .get("/api/unknown/23")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}
