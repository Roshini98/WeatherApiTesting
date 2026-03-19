package utils;

import java.util.Objects;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

public class ConfigLoader {
    public static String getProp(EnvironmentVariables vars, String key) {
    	
        String value = EnvironmentSpecificConfiguration.from(vars).getProperty(key);

        return Objects.requireNonNull(value, "ERROR: Missing '" + key + "' in serenity.conf");
    }
}