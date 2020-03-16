package ua.testing.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RecurseBundleConfig {
    private final static String PROGRAM_USER_PROPERTY_PATH = "src/main/resources/program_user_property.properties";

    @Bean
    public Properties programUserProperty() throws IOException {
        File file = new File(PROGRAM_USER_PROPERTY_PATH);
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        return properties;
    }
}
