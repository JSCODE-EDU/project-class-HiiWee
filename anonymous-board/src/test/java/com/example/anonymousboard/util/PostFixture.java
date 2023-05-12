package com.example.anonymousboard.util;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;

public class PostFixture {

    public static List<ExtractableResponse<Response>> httpPostSaveAll(final String... requestBodies) {
        return Arrays.stream(requestBodies)
                .map(PostFixture::httpPostSaveOne)
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> httpPostSaveOne(final String requestBody) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutUpdateOne(final String requestBody,
                                                                 final Long pathVariable) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(String.format("/posts/%d", pathVariable))
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> httpGetFindAll() {
        return given().log().all()
                .when()
                .get("/posts")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetFindOne(final Long pathVariable) {
        return given().log().all()
                .when()
                .get(String.format("/posts/%d", pathVariable))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteOne(final Long pathVariable) {
        return given().log().all()
                .when()
                .delete(String.format("/posts/%d", pathVariable))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetFindAllWithParameter(final String parameterName,
                                                                            final String parameterValue) {
        return given().param(parameterName, parameterValue).log().all()
                .when()
                .get("/posts")
                .then().log().all()
                .extract();
    }
}
