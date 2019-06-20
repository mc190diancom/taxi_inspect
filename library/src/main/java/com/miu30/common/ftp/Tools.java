package com.miu30.common.ftp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class Tools {
    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    /**
     * @param filename propertiy file path
     *
     * @author burkun
     */
    public static Properties readPropertiesFile(String filename) {
        Properties properties = new Properties();
        try {
            InputStream inputs = new FileInputStream(filename);
            properties.load(inputs);
            inputs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
