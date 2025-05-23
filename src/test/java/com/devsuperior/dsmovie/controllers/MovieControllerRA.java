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

public class MovieControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;

	@BeforeEach
	public void setUp() throws JSONException {
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = obtainAccessToken(clientUsername, clientPassword);
		adminToken = obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "invalid";
	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
				.header("Content-type", "application/json")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/movies")
				.then()
				.statusCode(200);
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		String movieTitle = "Venom";

		given()
				.header("Content-type", "application/json")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/movies?title={title}", movieTitle)
				.then()
				.statusCode(200)
				.body("content.id[0]", is(2))
				.body("content.title[0]", equalTo("Venom: Tempo de Carnificina"))
				.body("content.score[0]", is(3.3F))
				.body("content.count[0]", is(3))
				.body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		Long existingId = 1L;

		given()
				.header("Content-type", "application/json")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/movies/{id}", existingId)
				.then()
				.statusCode(200)
				.body("id", is(1))
				.body("title", equalTo("The Witcher"))
				.body("score", is(4.5F))
				.body("count", is(2))
				.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		Long existingId = 100L;

		given()
				.header("Content-type", "application/json")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/movies/{id}", existingId)
				.then()
				.statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		Map<String, Object> mapMovieRequest = new HashMap<>();
		mapMovieRequest.put("title", "");
		mapMovieRequest.put("score", 4.4);
		mapMovieRequest.put("count", 1);
		mapMovieRequest.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
		JSONObject newMovie = new JSONObject(mapMovieRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(422)
				.body("errors.message", hasItems("Tamanho deve ser entre 5 e 80 caracteres", "Campo requerido"));
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		Map<String, Object> mapMovieRequest = new HashMap<>();
		mapMovieRequest.put("title", "Test Movie");
		mapMovieRequest.put("score", 4.4);
		mapMovieRequest.put("count", 1);
		mapMovieRequest.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
		JSONObject newMovie = new JSONObject(mapMovieRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		Map<String, Object> mapMovieRequest = new HashMap<>();
		mapMovieRequest.put("title", "Test Movie");
		mapMovieRequest.put("score", 4.4);
		mapMovieRequest.put("count", 1);
		mapMovieRequest.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
		JSONObject newMovie = new JSONObject(mapMovieRequest);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + invalidToken)
				.body(newMovie)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.post("/movies")
				.then()
				.statusCode(401);
	}
}
