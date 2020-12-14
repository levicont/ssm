package com.lvg.ssm.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ApplicationProperties {
    private static final String APP_PROPERTIES_URL;

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
        throw new RuntimeException("No property with such name: "+propertyName);
    }
}
