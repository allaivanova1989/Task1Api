package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;

import static utils.PropertyReader.getProperty;


public class BaseTest {
    @BeforeSuite
    public void setup() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter( new ResponseLoggingFilter())
                .setBaseUri(getProperty("url"))
                .setContentType(ContentType.JSON)
                .build();
    }
}
