package com.nixsolutions.efimenko.laba13;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
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

public class JdbcRoleTest extends DatabaseTestCase {

    private JdbcRoleDao roleDao = new JdbcRoleDao();

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection connection = null;
        try {
            connection = new DatabaseConnection(
                    roleDao.createConnection());
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

    public JdbcRoleTest(String name) throws Throwable {
        super(name);
        
        InputStream inputStream = null;
        Properties properties = new Properties();
        inputStream = JdbcUserTest.class
                .getResourceAsStream(
                        "/DBresources/h2_test_connection.properties");
        properties.load(inputStream);
        inputStream.close();
        roleDao.setProperties(properties);
        Connection connection = null;
        Statement statement = null;

        try {
            connection = roleDao.createConnection();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Role("
                + "    id IDENTITY," + "    name VARCHAR2(50) NOT NULL UNIQUE,"
                + "    PRIMARY KEY (id)" + ");"
                + "CREATE TABLE IF NOT EXISTS User( " + "    id IDENTITY,  "
                + "    firstname VARCHAR2(20), "
                + "    lastname VARCHAR2(20), " + "    email VARCHAR2(20), "
                + "    login VARCHAR2(20) NOT NULL UNIQUE, "
                + "    password VARCHAR2(20), "
                + "    roleid BIGINT NOT NULL, " + " birthdate DATE, "
                + "    PRIMARY KEY (id) " + ");");
        statement.close();
        connection.close();
    }

    public void testCreateNull() throws Throwable {
        try {
            roleDao.create(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testCreate() throws Throwable {
        String roleName = "guest";
        Role role = new Role();
        role.setName(roleName);
        roleDao.create(role);

        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("Role");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/CreateRole.xml"));

        ITable expectedTable = expectedDataSet.getTable("Role");

        Assertion.assertEqualsIgnoreCols(expectedTable, actualTable,
                new String[] { "id" });
    }

    public void testRemoveNull() throws Throwable {
        try {
            roleDao.remove(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testRemove() throws Throwable {
        Role role = new Role();
        role.setId(1);
        roleDao.remove(role);
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("Role");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/RemoveRole.xml"));

        ITable expectedTable = expectedDataSet.getTable("Role");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testUpdateNull() throws Throwable {
        try {
            roleDao.remove(null);
            fail("Must throw NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    public void testUpdate() throws Throwable {
        String newRoleName = "guest";
        Role role = new Role();
        role.setId(1);
        role.setName(newRoleName);
        roleDao.update(role);
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("Role");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(JdbcUserTest.class
                        .getResourceAsStream("/DBresources/xml/UpdateRole.xml"));

        ITable expectedTable = expectedDataSet.getTable("Role");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testFindByNameThatExist() throws Throwable{
        String roleName = "admin";
        Role role = roleDao.findByName(roleName);
        TestCase.assertNotNull("Existing role was not found.", role);
    }

    public void testFindByNameThatNotExist() throws Throwable {
        String roleName = "Project Manager not exists";
        Role role = roleDao.findByName(roleName);
        TestCase.assertNull("Not existing role was  found.", role);
    }
}
