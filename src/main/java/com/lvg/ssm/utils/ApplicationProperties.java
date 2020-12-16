package com.lvg.ssm.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ApplicationProperties {
    private static final String APP_PROPERTIES_URL;
    public static final String OPEN_OFFICE_FILE_PATH_PREFIX = "file://";
    public static final String USER_HOME_PATH_PROPERTY_NAME = "user.home";

    static {
        APP_PROPERTIES_URL = Objects.requireNonNull(ApplicationProperties.class
                .getClassLoader().getResource("app.properties").getPath());
    }

    private static Properties properties;

    private static Properties getProperties(){
        if (null != properties)
            return properties;
        try {
            properties = new Properties();
            properties.load(new FileReader(APP_PROPERTIES_URL));

            return properties;
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }


    }

    public static String getProperty(String propertyName){
        Properties properties = getProperties();
        if (properties.containsKey(propertyName))
            return properties.getProperty(propertyName);
        Properties systemProperties = System.getProperties();
        if (systemProperties.containsKey(propertyName))
            return systemProperties.getProperty(propertyName);
        throw new RuntimeException("No property with such name: "+propertyName);
    }
}
