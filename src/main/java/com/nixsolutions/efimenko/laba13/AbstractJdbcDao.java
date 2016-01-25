package com.nixsolutions.efimenko.laba13;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.jdbcx.JdbcConnectionPool;

public abstract class AbstractJdbcDao {

    private static JdbcConnectionPool pool = null;
    private static Log fileLogger = LogFactory.getLog("db.dao.logger");
    private static Properties properties;

    public Connection createConnection() throws Throwable {
        if (pool == null) {
            InputStream inputStream = null;
            try {
                if (properties == null) {
                properties = new Properties();
                inputStream = AbstractJdbcDao.class
                        .getResourceAsStream(
                                "/DBresources/h2_connection.properties");
                properties.load(inputStream);
                inputStream.close();
                
                }
                Class.forName(properties.getProperty("driver"));
                pool = JdbcConnectionPool.create(properties.getProperty("url"),
                        properties.getProperty("user"),
                        properties.getProperty("password"));
                
                if (properties.getProperty("driver") != null 
                        && properties.getProperty("url") != null 
                        && properties.getProperty("user") != null
                        && properties.getProperty("password") != null) {
                    fileLogger.trace("Properties was loaded successfully");
                }
                fileLogger.warn("Connect to  " + "HOST: " 
                + properties.getProperty("url")  + " USER: " 
                        + properties.getProperty("user"));
                
            } catch (Exception ex) {
                fileLogger.error("Exception situation with loading of "
                        + "properties", ex);
                throw ex;
            }
        }
        return pool.getConnection();
    }
    
    public void setProperties(Properties propFile) {
        this.properties = propFile;
        
    }
}
