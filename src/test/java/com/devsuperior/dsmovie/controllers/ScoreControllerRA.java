package com.devsuperior.dsmovie.controllers;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.devsuperior.dsmovie.tests.TokenUtil.obtainAccessToken;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import java.util.List;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;

	@BeforeEach
	public void setUp() throws JSONException {
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";

		clientToken = obtainAccessToken(clientUsername, clientPassword);
		adminToken = obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "invalid";
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		Map<String, Object> mapScoreRequest = new HashMap<>();
		mapScoreRequest.put("movieId", 100);
		mapScoreRequest.put("score", 4);
		JSONObject newScore = new JSONObject(mapScoreRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		Map<String, Object> mapScoreRequest = new HashMap<>();
		mapScoreRequest.put("score", 4);
		JSONObject newScore = new JSONObject(mapScoreRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		Map<String, Object> mapScoreRequest = new HashMap<>();
		mapScoreRequest.put("movieId", 100);
		mapScoreRequest.put("score", -4);
		JSONObject newScore = new JSONObject(mapScoreRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
}
