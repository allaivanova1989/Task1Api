package tests;

import api.models.*;
import api.spec.BasicDetails;
import api.spec.Specifications;
import io.restassured.mapper.ObjectMapperType;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j2
public class apiTestPositive {

    private CreateAndUpdateModel createAndUpdateModel;
      private String token = "QpwL5tke4Pnpja7X4";
    private RegistrationAndLoginModel registrationAndLoginModel;

      public boolean isPresentDesiredElement(List<User> list, String email){
        for(int i =0; i< list.size();i++){
            if(list.get(i).getEmail().equalsIgnoreCase(email)){
                return true;
            }

        }

        return false;
    }

    @Test
    public void createUser() {
        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK201());
        log.info("user creation");
        createAndUpdateModel = CreateAndUpdateModel.builder()
                .name(BasicDetails.userName)
                .job("leader")
                .build();

        String nameOfUser = createAndUpdateModel.getName();
        String jobOfUser = createAndUpdateModel.getJob();

        Response responseOfCreatingUser = given()
                .body(createAndUpdateModel)
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(responseOfCreatingUser.getName(), nameOfUser);
        softAssertion.assertEquals(responseOfCreatingUser.getJob(), jobOfUser);
        softAssertion.assertAll();


        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK200());
        log.info("user update");
        createAndUpdateModel = CreateAndUpdateModel.builder()
                .name(nameOfUser)
                .job("zion resident")
                .build();

        Response responseForUpdatingJob = given()
                .body(createAndUpdateModel)
                .when()
                .put("/api/users/2")
                .then().log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        String newJobOfUser = responseForUpdatingJob.getJob();
        SoftAssert softAssertion2 = new SoftAssert();
        softAssertion2.assertEquals(newJobOfUser, createAndUpdateModel.getJob());
        softAssertion2.assertAll();

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK204());
        log.info("deleting a user");
        given()
                .when()
                .delete("/api/users/2")
                .then().log().all()
                .assertThat()
                .statusCode(204);
    }

    @Test
    public void successfulRegistration(){

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK200());

        log.info("user registration");
        registrationAndLoginModel = RegistrationAndLoginModel.builder()
                .email(BasicDetails.successfulEmail)
                .password(BasicDetails.successfulPassword)
                .build();

        Response responseAboutRegistration = given()
                .body(registrationAndLoginModel)
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertTrue(responseAboutRegistration.getId()!=0);
        softAssertion.assertTrue(responseAboutRegistration.getToken()!=null);
        softAssertion.assertAll();

        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK200());
         log.info("check if the required user is in the list");
        List<User> users = given()
                .when()
                .get("/api/users?page=1")
                .then().log().all()
                .extract().body().jsonPath().getList("data",User.class);


        assertTrue(isPresentDesiredElement(users, BasicDetails.successfulEmail));

    }

    @Test
    public void SuccessfulLogin(){
        Specifications.installSpecification(Specifications.requestSpec(BasicDetails.URL),Specifications.responseSpecOK200());

        log.info("user logs in");
        registrationAndLoginModel = RegistrationAndLoginModel.builder()
                .email(BasicDetails.successfulEmail)
                .password("cityslicka")
                .build();


        Response responseForLogin = given()
                .body(registrationAndLoginModel)
                .when()
                .post("/api/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(Response.class, ObjectMapperType.GSON);

        assertEquals(responseForLogin.getToken(),token);
    }


}
