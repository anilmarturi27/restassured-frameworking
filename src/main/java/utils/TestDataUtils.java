
package utils;


import java.io.IOException;
import java.io.InputStream;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

public class TestDataUtils {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    public static JsonNode getTestData(String section) {

        try (InputStream input = TestDataUtils.class
                .getClassLoader()
                .getResourceAsStream("testdata/booking-data.json")) {

            if (input == null) {
                throw new RuntimeException(
                        "booking-data.json not found");
            }

            JsonNode root = OBJECT_MAPPER.readTree(input);
            JsonNode data = root.get(section);

            if (data == null) {
                throw new IllegalArgumentException(
                        "Test data section not found: " + section);
            }

            return data;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to read booking test data", e);
        }
    }
}
