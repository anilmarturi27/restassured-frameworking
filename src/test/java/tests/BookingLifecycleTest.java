package tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.qameta.allure.testng.AllureTestNg;

import base.BaseTest;
import config.ConfigManager;
import dataProviders.BookingDataProvider;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonMappingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import models.BookingDates;
import models.BookingRequest;
import services.BookingService;
import utils.TestDataUtils;


@Listeners(AllureTestNg.class)
public class BookingLifecycleTest extends BaseTest {
	
	protected int BookingId;
	private String Otoken;
	
	@Test(priority=1)
	public void getToken() {
		String username = ConfigManager.getProperty("username");
		String password = ConfigManager.getProperty("password");
		
		String token = bookingService.createAuthToken(username, password);
		
		Assert.assertNotNull(token, "Token should not be null");
	    Assert.assertFalse(token.isBlank(), "Token should not be empty");
	    
	    Otoken = token;
	    
//	    System.out.println(Otoken);
		
	}
	
	@Test(priority=2)
	public void createBooking() throws JsonMappingException, JsonProcessingException {
		
		String jsonData = TestDataUtils.getTestData("createBooking").toString();
		
		ObjectMapper mapper = new ObjectMapper();
		BookingRequest bookingRequest = mapper.readValue(jsonData, BookingRequest.class);
		
//		System.out.println("Booking JSON: " + jsonData);
//		System.out.println("Booking object: " +
//		        new ObjectMapper().writeValueAsString(bookingRequest));
		
		
		Response response = bookingService.createBooking(bookingRequest);
		
		Assert.assertEquals(response.statusCode(), 200);
		
		Assert.assertNotNull(response.jsonPath().getInt("bookingid"));
		
		BookingId = response.jsonPath().getInt("bookingid");
		
		response.then().assertThat()
        .body(matchesJsonSchemaInClasspath(
                "schemas/create-booking-schema.json"));
		
//		System.out.println("Booking ID: " + BookingId);
		
//		System.out.println(response.asPrettyString());
		
			
	}
	
	@Test(priority=3)
	public void getBookings() {
		
		Response response = bookingService.getBooking(BookingId);
		
		Assert.assertEquals(response.statusCode(), 200);
		
		Assert.assertNotNull(response.jsonPath().getString("firstname"));
		
		Assert.assertNotNull(response.jsonPath().getString("lastname"));
		
		Assert.assertNotNull(response.jsonPath().get("bookingdates"));
		
		response.then().assertThat()
        .body(matchesJsonSchemaInClasspath(
                "schemas/get-booking-schema.json"));
		
//		System.out.println(response.asPrettyString());
		
	}
	
	
	@Test(priority=4)
	public void updateBooking() throws JsonMappingException, JsonProcessingException {
		
		String jsonData = TestDataUtils.getTestData("updateBooking").toString();
		
		ObjectMapper mapper = new ObjectMapper();
		BookingRequest bookingRequest = mapper.readValue(jsonData, BookingRequest.class);
		
		Response response = bookingService.updateBooking(BookingId,Otoken,bookingRequest);
		
		Assert.assertEquals(response.statusCode(), 200);
		
		Assert.assertEquals(
	            response.jsonPath().getString("firstname"),
	            bookingRequest.getFirstname());

	    Assert.assertEquals(
	            response.jsonPath().getString("lastname"),
	            bookingRequest.getlastname());
		
//		System.out.println(response.asPrettyString());
		
		
	}
	
	
	@Test(priority=5)
	public void patchBooking() throws JsonMappingException, JsonProcessingException {
		
		String jsonData = TestDataUtils.getTestData("patchBooking").toString();
		
		ObjectMapper mapper = new ObjectMapper();
		BookingRequest bookingRequest = mapper.readValue(jsonData, BookingRequest.class);
		
		Response response = bookingService.patchBooking(BookingId,Otoken,bookingRequest.getFirstname());
		
		Assert.assertEquals(response.statusCode(), 200);
		
		Assert.assertEquals(
	            response.jsonPath().getString("firstname"),
	            bookingRequest.getFirstname());
		
	}
	
	
	@Test(priority=6, dependsOnMethods= {"getToken","createBooking"})
	public void deleteBooking() {
		
		Response response = bookingService.deleteBooking(BookingId,Otoken);
		
		Response getResponse = bookingService.getBooking(BookingId);
		
		getResponse.then().statusCode(404);
		
		Assert.assertEquals(response.statusCode(), 201);
		
		
	}
	
//	@Test(dataProvider = "bookingData" , dataProviderClass = BookingDataProvider.class)
//	public void createBookingWithDataProvider(String firstname,String lastname,int totalprice) {
//		
//		BookingDates bookingDates = new BookingDates("2026-11-01", "2026-11-05");
//		
//		BookingRequest bookingRequest = new BookingRequest(
//	            firstname,
//	            lastname,
//	            totalprice,
//	            true,
//	            bookingDates,
//	            "Breakfast"
//	    );
//		
//		Response response = bookingService.createBooking(bookingRequest);
//		
//		int bookingId = response.jsonPath().getInt("bookingid");
//		
//		Assert.assertEquals(
//	            response.jsonPath().getString("booking.firstname"),
//	            firstname
//	    );
//		
//		Assert.assertEquals(
//	            response.jsonPath().getString("booking.lastname"),
//	            lastname
//	    );
//		
//		System.out.println(
//				response.jsonPath().getInt("bookingid")
//	    );
//		
//	}
}
