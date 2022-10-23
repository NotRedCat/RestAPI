package tests;

import models.ResponseLombokModel;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static specs.TestSpecs.*;

public class RequestInTests {

    @Test
    @DisplayName("Проверка, что пользователь с почтой eve.holt@reqres.in есть в списке")
    void singleUserTest() {
        given()
                .spec(testRequestSpec)
                .when()
                .get("/api/users")
                .then()
                .spec(testResponseSpec200)
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
    }
    @Test
    @DisplayName("Проверка, что пользователь с Emma есть в списке пользователей")
    void singleUserNameTest() {
        given()
                .spec(testRequestSpec)
                .when()
                .get("/api/users")
                .then()
                .spec(testResponseSpec200)
                .body("data.findAll{it.first_name}.first_name.flatten()",
                        hasItem("Emma"));
    }

    @Test
    @DisplayName("Пользователя с id = 45, не существует")
    void userNotFoundTest() {

        given()
                .spec(testRequestSpec)
                .when()
                .get("/api/users")
                .then()
                .spec(testResponseSpec200)
                .body("data.findAll{it.id!=null}.id.flatten()",
                        not(hasItem(45)));
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUserTest() {
        User body = new User();
        body.setName("Jack");
        body.setJob("teacher");
        ResponseLombokModel response = given()
                .spec(testRequestSpec)
                .body(body)
                .when()
                .post("/api/users")
                .then()
                .spec(testResponseSpec201)
                .extract()
                .as(ResponseLombokModel.class);
        assertThat(response.getName()).isEqualTo(body.getName());
    }


    @Test
    @DisplayName("Изменение существующего пользователя")
    void changeUserTest() {
        User body1 = new User();
        User body2 = new User();
        body1.setName("Jack");
        body1.setJob("teacher");
        body2.setNewName("Jim");
        body2.setNewJob("driver");
        ResponseLombokModel response = given()
                .spec(testRequestSpec)
                .body(body1)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .spec(testResponseSpec201)
                .extract()
                .as(ResponseLombokModel.class);
        assertThat(response.getName()).isEqualTo(body1.getName());
        assertThat(response.getName()).isEqualTo(body1.getJob());
        ResponseLombokModel response2 = given()
                .spec(testRequestSpec)
                .body(body2)
                .when()
                .put("https://reqres.in/api/user/" + response.getId())
                .then()
                .spec(testResponseSpec200)
                .extract()
                .as(ResponseLombokModel.class);
        assertThat(response2.getNewName()).isEqualTo(body2.getNewName());
        assertThat(response2.getNewJob()).isEqualTo(body2.getNewJob());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        User body = new User();
        body.setName("Jack");
        body.setJob("teacher");
        ResponseLombokModel response = given()
                .spec(testRequestSpec)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .spec(testResponseSpec201)
                .extract()
                .as(ResponseLombokModel.class);
        given()
                .spec(testRequestSpec)
                .when()
                .delete("https://reqres.in/api/users/" + response.getId())
                .then()
                .spec(testResponseSpec204);
        given()
                .spec(testRequestSpec)
                .when()
                .get("https://reqres.in/api/users/" + response.getId())
                .then()
                .spec(testResponseSpec404);
    }

}
