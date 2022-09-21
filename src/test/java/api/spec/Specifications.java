package api.spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {
    public static RequestSpecification requestSpec(String url) {

        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
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
