package api.spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.IOException;

import static utils.Constants.PropertyName.URL;
import static utils.PropertyReader.getProperty;

public class Specifications {
    public static RequestSpecification requestSpec() throws IOException {

        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setBaseUri(getProperty(URL))
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;

    }

    public static void installSpecification(RequestSpecification request) {
        RestAssured.requestSpecification = request;

    }
}
