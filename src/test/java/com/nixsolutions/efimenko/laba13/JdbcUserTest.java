package com.nixsolutions.efimenko.laba13;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.dbunit.Assertion;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;

import com.nixsolutions.efimenko.laba13.JdbcRoleTest;
import com.nixsolutions.efimenko.laba13.JdbcUserDao;
import com.nixsolutions.efimenko.laba13.User;

public class JdbcUserTest extends DatabaseTestCase {

    private static final JdbcUserDao userDao = new JdbcUserDao();

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection connection = null;
        try {
            connection = new DatabaseConnection(
                    userDao.createConnection());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                new H2DataTypeFactory());

        return connection;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(JdbcRoleTest.class
                .getResourceAsStream("/DBresources/xml/Tables.xml"));
    }

    public JdbcUserTest(String name) throws Throwable {
        super(name);
        
        InputStream inputStream = null;
        Properties properties = new Properties();
        inputStream = JdbcUserTest.class
                .getResourceAsStream(
                        "/DBresources/h2_test_connection.properties");
        properties.load(inputStream);
        inputStream.close();
        userDao.setProperties(properties);

        Connection connection = null;
        Statement statement;

        try {
            connection = userDao.createConnection();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Role("
                + "    id BIGINT IDENTITY,"
                + "    name VARCHAR(50) NOT NULL UNIQUE,"
                + "    PRIMARY KEY (id)" + ");"
                + "CREATE TABLE IF NOT EXISTS User( "
                + "    id BIGINT IDENTITY,  " + "    firstname VARCHAR(20), "
                + "    lastname VARCHAR(20), " + "    email VARCHAR(20), "
                + "    login VARCHAR(20) NOT NULL UNIQUE, "
                + "    password VARCHAR(20), " + "    roleid BIGINT NOT NULL, "
                + "    birthdate DATE, " + "    PRIMARY KEY (id) " + ");");
        statement.close();
        connection.close();
    }

    public void testCreateNull() throws Throwable {
        try {
            userDao.create(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }
    
    public void testCreate() throws Throwable {
        @SuppressWarnings("deprecation")
        User user = new User(6l, "Vova", "Vladimirov", "vova@qwe.wqe", "vova",
                "Password", 1l, new Date(99, 0, 1));
        userDao.create(user);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("User");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/CreateUser.xml"));

        ITable expectedTable = expectedDataSet.getTable("User");

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable,
                new String[] { "id" });
    }
    
    public void testRemoveNull() throws Throwable {
        try {
            userDao.remove(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testRemove() throws Throwable {
        User user = new User(1l, "", "", "", "", "", null, null);
        userDao.remove(user);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("User");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/RemoveUser.xml"));

        ITable expectedTable = expectedDataSet.getTable("User");

        Assertion.assertEquals(expectedTable, actualTable);
    }
    
    public void testUpdateNull() throws Throwable {
        try {
            userDao.update(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testUpdate() throws Throwable {
        User user = new User(1l, "newFirstName", "newLastName", "newEmail", "newLogin",
                "newPassword", 1l, new Date(90, 0, 1));

        userDao.update(user);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("User");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/UpdateUser.xml"));

        ITable expectedTable = expectedDataSet.getTable("User");

        Assertion.assertEquals(expectedTable, actualTable);
    }
    
    public void testFindByEmailNull() throws Throwable {
        try {
            userDao.findByEmail(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testFindByEmail() throws Throwable {
        User user = userDao.findByEmail("sidor@qwe.qwe");
        TestCase.assertEquals("Existing user was not found.", 4l, user.getId());
    }

    public void testFindByLoginNull() throws Throwable {
        try {
            userDao.findByLogin(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }
    
    public void testFindByLogin() throws Throwable {
        User user = userDao.findByLogin("vanya");
        TestCase.assertEquals("Existing user was not found.", 2, user.getId());
    }

    public void testFindAll() throws Throwable {
        List<User> userList = userDao.findAll();
        TestCase.assertEquals("Quantity of users were get is different "
                + "from actual.", 4, userList.size());
    }
    
    @Override
    protected void tearDown() throws Exception {
        userDao.setProperties(null);
    }

}
