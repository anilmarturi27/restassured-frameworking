package dataProviders;

import java.io.InputStream;

import org.testng.annotations.DataProvider;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

public class BookingDataProvider {
	
	@DataProvider(name = "bookingData")
    public Object[][] getBookingData() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("testdata/booking-datasets.json");

        if (input == null) {
            throw new RuntimeException(
                    "booking-datasets.json was not found");
        }

        JsonNode root = mapper.readTree(input);
        JsonNode bookings = root.get("bookings");

        Object[][] data = new Object[bookings.size()][3];

        for (int i = 0; i < bookings.size(); i++) {
            JsonNode booking = bookings.get(i);

            data[i][0] = booking.get("firstname").asText();
            data[i][1] = booking.get("lastname").asText();
            data[i][2] = booking.get("totalprice").asInt();
        }

        input.close();

        return data;
    }
}
