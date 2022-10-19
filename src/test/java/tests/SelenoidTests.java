package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class SelenoidTests {
    /*
     1. make request to https://selenoid.autotests.cloud/status
     2. get response {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"android":{"8.1":{}},"chrome":
     {"100.0":{},"99.0":{}},"chrome-mobile":{"86.0":{}},"firefox":{"97.0":{},"98.0":{}},"opera":{"84.0":{},"85.0":{}}}}
     3. check total is 20
  */
    @Test
    void checkTotal() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithGiven() {
        given().
                when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithSomeLogs() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkChromeVersion() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalGoodPractice() {
        Integer expectedTotal = 20;
        Integer actualTotal = given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("total");
    }

    /*
  1. make request to https://selenoid.autotests.cloud/wd/hub/status
  2. get response {"value":{"message":"Selenoid 1.10.7 built at 2021-11-21_05:46:32AM","ready":true}}
  3. check value.ready is true
*/
    @Test
    void checkWDStatus() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401);
    }

    @Test
    void checkWDTotal() {
        given()
                .log().uri()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void checkWDHubStatus() {
        given()
                .auth().basic("user1", "1234")
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}