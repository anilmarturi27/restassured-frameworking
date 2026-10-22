package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	public static final Properties properties = new Properties();
	
	static {
		try(InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
			if(input==null) {
				throw new RuntimeException("config.properties file is not found");
			}
			
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
	                "Failed to load config.properties", e);
		}
	}
	
	public static String getProperty(String key) {
		String value = properties.getProperty(key);
		if(value==null || value.isBlank()) {
			throw new IllegalArgumentException(
	                "Missing configuration property: " + key);
		}
		return value.trim();
	}
}
