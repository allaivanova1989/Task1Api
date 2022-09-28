package api.spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import static utils.PropertyReader.getPropertyByName;

public class Specifications {


    public static void installSpecification() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setBaseUri(getPropertyByName("url"))
                .setContentType(ContentType.JSON)
                .build();


    }
}
