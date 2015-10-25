package com.softarex.jworker.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used for loading redis config from the redis.properties file
 * 
 * @author Ivan Dubynets
 * @email ivan@softarex.com
 */
public class RedisConfig {
    public static final String REDISDATABASE_PARAM = "redis.database";
    public static final String REDISPORT_PARAM     = "redis.port";
    public static final String REDISHOST_PARAM     = "redis.host";
    public static final String REDISPROPERTIES     = "redis.properties";
    
    private String host="localhost";
    private int port=6379;
    private int database=5;
    
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    public RedisConfig(String propertyFile) throws IOException {
        loadProperties(propertyFile);
    }

    public RedisConfig() {
        try {
            this.loadProperties(null);
        } catch (IOException ex) {
            logger.debug("can't load config", ex);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
    
    public Config buildJesqueConfig() {
        return new ConfigBuilder().withDatabase(this.getDatabase())
                .withHost(this.getHost())
                .withPort(this.getPort()).build();
    }
    
    private void loadProperties(String propertyFile) throws IOException, NumberFormatException {
        Properties props = this.getProperties(propertyFile);
        this.host = props.getProperty(REDISHOST_PARAM, this.host);
        this.port = Integer.valueOf(props.getProperty(REDISPORT_PARAM, String.valueOf(this.port)));
        this.database = Integer.valueOf(props.getProperty(REDISDATABASE_PARAM, String.valueOf(this.database)));
    }
    
    private Properties getProperties(String propertyFile) throws IOException {
        String resourceName = propertyFile == null ? REDISPROPERTIES : propertyFile;
        Properties props = new Properties();
        try {
            try (FileInputStream inStream = new FileInputStream("./" + resourceName)) {
                props.load(inStream);
            }
        }
        catch(Exception e) {
            // Means we run from IDE
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                props.load(resourceStream);
            }
        }
        return props;
    }
}
