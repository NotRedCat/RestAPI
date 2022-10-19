package tests;

import netscape.javascript.JSObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {

    @Test
    @DisplayName("Получение информации о пользователе")
    void SingleUserTest() {
        given()
                .when()
                .get("https://reqres.in/api/users/7")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Michael"))
                .body("data.last_name", is("Lawson"));
    }


    @Test
    @DisplayName("Пользователь не найден")
    void UserNotFoundTest() {
        given()
                .when()
                .get("https://reqres.in/api/users/45")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void CreateUserTest() {
        String body = "{ \"name\": \"Jack\", \"job\": \"teacher\" }";
        given()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Jack"))
                .body("job", is("teacher"));

    }

    @Disabled
    @Test
    @DisplayName("Создание пустого пользователя" +
            "Тут я ожидала, что возникнет какая-нибудь ошибка, но пустой пользователь сохранился." +
            "Думаю это полезный тест, чтобы понять, что в базу сохраняются пустые пользователи, " +
            "и нужно это исправлять")
    void CreateEmptyUserTest() {
        String body = "{}";
        given()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    @DisplayName("Изменение существующего пользователя")
    void ChangeUserTest() {
        String body1 = "{ \"name\": \"Jack\", \"job\": \"teacher\" }";
        String body2 = "{ \"name\": \"Jim\", \"job\": \"driver\" }";

        var id = given()
                .log().body()
                .contentType(JSON)
                .body(body1)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Jack"))
                .body("job", is("teacher"))
                .extract()
                .response()
                .path("id");
        given()
                .log().body()
                .contentType(JSON)
                .body(body2)
                .when()
                .put("https://reqres.in/api/user/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Jim"))
                .body("job", is("driver"));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void DeleteUserTest() {
        String body = "{ \"name\": \"Jack\", \"job\": \"teacher\" }";
        var id = given()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract()
                .response()
                .path("id");
        given()
                .log().body()
                .when()
                .delete("https://reqres.in/api/users/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
        given()
                .when()
                .get("https://reqres.in/api/users/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

}
