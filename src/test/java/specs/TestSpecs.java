package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestSpecs {
    public static RequestSpecification testRequestSpec = with()
            .filter(withCustomTemplates())
            .baseUri("https://reqres.in")
            .log().body()
            .contentType(ContentType.JSON);

    public static ResponseSpecification testResponseSpec201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .expectBody("id", notNullValue())
            .build();

    public static ResponseSpecification testResponseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();

    public static ResponseSpecification testResponseSpec204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();

    public static ResponseSpecification testResponseSpec404 = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .build();
}