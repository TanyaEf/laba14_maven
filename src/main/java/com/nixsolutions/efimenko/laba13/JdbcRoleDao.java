package com.nixsolutions.efimenko.laba13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nixsolutions.efimenko.laba13.interfaces.RoleDao;

public class JdbcRoleDao extends AbstractJdbcDao implements RoleDao {

    private Log fileLogger = LogFactory.getLog("db.dao.logger");
    private final String insertSql = "insert  into role (NAME) values (?);";
    private final String deleteSql = "delete from role where ID=?;";
    private final String updateSql = "update role set NAME=? where ID=? ;";
    private final String selectByNameSql = "select * from role where NAME= ?;";

    @Override
    public void create(Role role) throws Throwable{

        if (role == null) {
            fileLogger.warn("Object Role was null, new row was not added to "
                    + "Role table");
            throw new NullPointerException();
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            statement = connection.prepareStatement(insertSql);
            statement.setString(1, role.getName());
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("Role was added successfully");
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
    public void update(Role role) throws Throwable{

        if (role == null) {
            fileLogger.warn("Object Role was null, row was not updated in  "
                    + "Role table");
            throw new NullPointerException();
        }
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            statement = connection.prepareStatement(updateSql);
            statement.setString(1, role.getName());
            statement.setLong(2, role.getId());
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("Role was updated successfully");
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
    public void remove(Role role) throws Throwable {

        if (role == null) {
            fileLogger.warn("Object Role was null, row was not removed from  "
                    + "Role table");
            throw new NullPointerException();
        }
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = createConnection();
            statement = connection
                    .prepareStatement(deleteSql);
            statement.setLong(1, role.getId());
            if (statement.executeUpdate() == 1) {
                fileLogger.trace("Role was removed successfully");
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
    public Role findByName(String name) throws Throwable {
        
        if (name  == null) {
            fileLogger.warn("Role was not found ");
            throw new NullPointerException();
        }
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        Role resultRole = null;

        try {
            connection = createConnection();
            statement = connection
                    .prepareStatement(selectByNameSql);
            statement.setString(1, name);
            result = statement.executeQuery();

            if (result.next()) {
                resultRole = new Role(result.getLong("id"), result.getString("name"));
            }
            if (resultRole != null) {
                fileLogger.trace("Role was found successfully");
                return resultRole;
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
