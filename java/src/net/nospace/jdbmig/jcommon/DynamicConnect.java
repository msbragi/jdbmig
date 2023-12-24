package net.nospace.jdbmig.jcommon;

import net.nospace.jdbmig.jmodel.Config;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DynamicConnect {

    private Connection connection;

    public DynamicConnect(Config config) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        connection = null;
        String jarfile = config.useDriver().getJarFile() + "!/";
        URL u = new URL("jar:file:" + jarfile);
        String classname = config.useDriver().getClassName();
        URLClassLoader ucl = new URLClassLoader(new URL[]{u});
        Driver d = (Driver) Class.forName(classname, true, ucl).newInstance();
        DriverManager.registerDriver(new DynamicDriver(d));
        if(config.getConnection().getUser() == null || config.getConnection().getUser().equals("")) {
            connection = DriverManager.getConnection(config.getConnection().getJdbcUrl());
        } else {
            connection = DriverManager.getConnection(config.getConnection().getJdbcUrl(), config.getConnection().getUser(), config.getConnection().getPassword());
        }
        initStrings(config.getConnection().getInitStrings());
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void initStrings(List<String> scripts) throws SQLException {
        if (scripts == null) {
            return;
        }
        for (String sql : scripts) {
            execute(sql);
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if (sql == null || sql.trim().equals("")) {
            return null;
        }
        Statement stmt = this.connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void execute(String sql) throws SQLException {
        if (sql == null || sql.trim().equals("")) {
            return;
        }
        Statement stmt = this.connection.createStatement();
        stmt.execute(sql);
    }

}
