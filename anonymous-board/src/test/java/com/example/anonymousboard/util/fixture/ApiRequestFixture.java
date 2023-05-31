package com.example.anonymousboard.util.fixture;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;

public class ApiRequestFixture {

    public static ExtractableResponse<Response> httpPost(final Object requestBody, final String path) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static List<ExtractableResponse<Response>> httpPostAllWithAuthorization(final String path,
                                                                                   final String token,
                                                                                   final Object... requestBodies) {
        return Arrays.stream(requestBodies)
                .map(requestBody -> httpPostWithAuthorization(requestBody, path, token))
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> httpPostWithAuthorization(final Object requestBody, final String path,
                                                                          final String token) {
        return given().log().all()
                .body(requestBody)
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpPutUpdateOne(final Object requestBody,
                                                                 final String path,
                                                                 final String token) {
        return given().log().all()
                .body(requestBody)
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> httpGet(final String path) {
        return given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpDeleteOne(final String path, final String token) {
        return given().log().all()
                .header(AUTHORIZATION, token)
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> httpGetFindAllWithParameter(final String path,
                                                                            final String parameterName,
                                                                            final String parameterValue) {
        return given().param(parameterName, parameterValue).log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }
}
