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
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
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
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
