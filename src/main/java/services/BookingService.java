package services;

import io.restassured.response.Response;
import models.BookingRequest;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import io.restassured.path.json.JsonPath;
import clients.ApiClient;

@SuppressWarnings("unused")
public class BookingService {
	
	public String createAuthToken(String username, String password) {
		
		
//		String requestBody = String.format(
//	            "{\"username\":\"%s\",\"password\":\"%s\"}",
//	            username, password);
		
		HashMap<String,String> requestBody = new HashMap<>();
		
		requestBody.put("username", username);
		requestBody.put("password", password);

		
		Response response = 
				given()
					.spec(ApiClient.getRequestSpec())
					.body(requestBody)
				.when()
					.post("/auth");
		
		response.then().statusCode(200);
		
//		System.out.println("Status Code: " + response.statusCode());
//	    System.out.println("Response Body: " + response.asString());
		
		String token = response.jsonPath().getString("token");
		
		return token;
					
	}
	
	public Response createBooking(BookingRequest bookingRequest) {
		
		return given()
				.spec(ApiClient.getRequestSpec())
				.body(bookingRequest)
//				.log().all()
				
				.when()
					.post("/booking");
							
	}
	
	public Response getBooking(int bookingId) {
		
		return given()
				.spec(ApiClient.getRequestSpec())
			
			.when()
				.get("/booking/" + bookingId);
	}
	
	public Response updateBooking(int bookingId,String token,BookingRequest bookingRequest) {
		
		return given()
				.spec(ApiClient.getRequestSpec())
				.body(bookingRequest)
				.header("Cookie","token="+token)
				
				.when()
					.put("/booking/" + bookingId);
						
	}
	
	public Response patchBooking(int bookingId,String token,String firstName) {
		
		String requestBody = String.format(
                "{\"firstname\":\"%s\"}", firstName);
		
		return given()
				.spec(ApiClient.getRequestSpec())
				.header("Cookie","token="+token)
				.body(requestBody)
				
			.when()
				.patch("/booking/" + bookingId);
				
	}
	
	public Response deleteBooking(int bookingId,String token) {
		
		return given()
                .spec(ApiClient.getRequestSpec())
                .header("Cookie", "token=" + token)
        .when()
                .delete("/booking/" + bookingId);
	}

}
