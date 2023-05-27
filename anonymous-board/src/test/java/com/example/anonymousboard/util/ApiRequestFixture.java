package com.example.anonymousboard.util;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;

public class ApiRequestFixture {

    public static List<ExtractableResponse<Response>> httpPostSaveAll(final String path, final String... requestBodies) {
        return Arrays.stream(requestBodies)
                .map(requestBody -> httpPostSaveOne(requestBody, path))
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> httpPostSaveOne(final String requestBody, final String path) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutUpdateOne(final String path,
                                                                 final String requestBody,
                                                                 final Long pathVariable) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(String.format(path, pathVariable))
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> httpGetFindAll(final String path) {
        return given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetFindOne(final String path, final Long pathVariable) {
        return given().log().all()
                .when()
                .get(String.format(path, pathVariable))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteOne(final String path, final Long pathVariable) {
        return given().log().all()
                .when()
                .delete(String.format(path, pathVariable))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetFindAllWithParameter(final String path,
                                                                            final String parameterName,
                                                                            final String parameterValue) {
        return given().param(parameterName, parameterValue).log().all()
                .when()
                .get("/posts")
                .then().log().all()
                .extract();
    }
}
