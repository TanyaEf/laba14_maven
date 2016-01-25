package com.nixsolutions.efimenko.laba13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nixsolutions.efimenko.laba13.interfaces.UserDao;
public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private Log fileLogger = LogFactory.getLog("db.dao.logger");
    
    private final String insertSql = "insert  into user (LOGIN, PASSWORD,"
            + " EMAIL, FIRSTNAME, LASTNAME, ROLEID, BIRTHDATE) "
            + "values (?,?,?, ?, ?, ?, ?);";
    private final String selectAllSql = "select * from user;";
    private final String deleteSql = "delete from user where ID=?";
    private final String updateSql = "update user set LOGIN=?, PASSWORD =?, "
            + "EMAIL = ?, FIRSTNAME =?, LASTNAME =?, BIRTHDATE=?, ROLEID = ? "
            + "WHERE ID=?;";
   

    private final String selectByLoginSql = "select * from user "
            + "where LOGIN = ?;";
    private final String selectByEmailSql = "select * from user "
            + "where EMAIL = ?;";

    public JdbcUserDao() {
    }

    @Override
    public void create(User user) throws Throwable {
        
        if (user == null) {
            fileLogger.warn("Object User was null, new row was not added to "
                    + "User table");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            if (findByLogin(user.getLogin()) != null 
                    && findByEmail(user.getEmail()) != null) {
                fileLogger.error("User is already exists:");
            }
            statement = connection.prepareStatement(insertSql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setLong(6, user.getRole());
            statement.setDate(7,
                    new java.sql.Date(user.getBirthDate().getTime()));
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("User was updated successfully");
            }
            connection.commit();

        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            
            }
        }
    }

    @Override
    public void update(User user) throws Throwable {
        
        if (user == null) {
            fileLogger.warn("Object User was null, new row was not updated in "
                    + "User table");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            statement = connection.prepareStatement(updateSql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setDate(6,
                    new java.sql.Date(user.getBirthDate().getTime()));
            statement.setLong(7, user.getRole());
            statement.setLong(8, user.getId());
            
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("User was updated successfully");
            }
            connection.commit();
        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            
            }
        }
    }

    @Override
    public void remove(User user) throws Throwable {
        
        if (user == null) {
            fileLogger.warn("Object User was null, new row was not removed"
                    + " from User table");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(deleteSql);
            statement.setLong(1, user.getId());
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("User was removed successfully");
            }
            connection.commit();
        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            
            }
        }
    }

    @Override
    public List<User> findAll() throws Throwable {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> userList = null;;

        try {
            connection = createConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectAllSql);
            userList = new ArrayList<>();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getLong(1), 
                        resultSet.getString(2), 
                        resultSet.getString(3),
                        resultSet.getString(4), 
                        resultSet.getString(5), 
                        resultSet.getString(6), 
                        resultSet.getLong(7), 
                        new Date(resultSet.getDate(8).getTime())));
            }
            return userList;
        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            }
        }
    }

    @Override
    public User findByLogin(String login) throws Throwable {
        
        if (login == null) {
            fileLogger.warn("Object User was not found, argument is empty");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(selectByLoginSql);
            statement.setString(1, login);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fileLogger.trace("User was found successfully");
                return new User(resultSet.getLong(1), resultSet.getString(2), 
                        resultSet.getString(3), resultSet.getString(4), 
                        resultSet.getString(5), resultSet.getString(6), 
                        resultSet.getLong(7), 
                        new Date(resultSet.getDate(8).getTime()));
            } else {
                fileLogger.warn("Object User was not found");
            }
            
        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws Throwable  {

        if (email == null) {
            fileLogger.warn("Object User was null, new row was not added to "
                    + "User table");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(selectByEmailSql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getLong(1), resultSet.getString(2), 
                        resultSet.getString(3), resultSet.getString(4), 
                        resultSet.getString(5), resultSet.getString(6), 
                        resultSet.getLong(7), 
                        new Date(resultSet.getDate(8).getTime()));
            } else {
                fileLogger.warn("Object User was not found");
            }
            
        } catch (SQLException ex) {
            fileLogger.error("Exception situation", ex);
            throw ex;
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                fileLogger.error("Resources were not closed", e);
            }
        }
        return null;
    }
}
