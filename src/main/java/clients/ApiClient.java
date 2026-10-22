package clients;

import config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
	public static final RequestSpecification REQUEST_SPEC = (RequestSpecification) new RequestSpecBuilder()
			.setBaseUri(ConfigManager.getProperty("base.url"))
			.setContentType(ContentType.JSON)
			.setAccept("application/json")
			.addFilter(new AllureRestAssured())
			.addFilter(new ErrorLoggingFilter())
			.build();
	
	public static RequestSpecification getRequestSpec() {
		return REQUEST_SPEC;
	}
}
