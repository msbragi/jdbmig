package net.nospace.jdbmig.jmodel;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private String dataDir;
    private Boolean fieldToLowerCase;
    private Boolean prettyPrint;
    private String useConn;
    private List<String> tables;
    private ConnectionTo connection;
    private List<DriverTo> drivers;
    private String bootstrap;
    private String shutdown;

    public Config() {
        useConn = null;
        bootstrap = null;
        shutdown = null;
        drivers = new ArrayList<>();
        tables = new ArrayList<>();
        fieldToLowerCase = false;
        prettyPrint = false;
    }

    public ConnectionTo getConnection() {
        return connection;
    }

    public void setConnection(ConnectionTo connection) {
        this.connection = connection;
    }

    public List<DriverTo> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverTo> drivers) {
        this.drivers = drivers;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        if (dataDir != null) {
            dataDir = dataDir.endsWith("/") ? dataDir : dataDir + "/";
        }
        this.dataDir = dataDir;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public String getType() {
        return this.connection.getType();
    }

    public Boolean getFieldToLowerCase() {
        return fieldToLowerCase;
    }

    public void setFieldToLowerCase(Boolean fieldToLowerCase) {
        this.fieldToLowerCase = fieldToLowerCase;
    }

    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public String getUseConn() {
        return useConn;
    }

    public void setUseConn(String useConn) {
        this.useConn = useConn;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public String getShutdown() {
        return shutdown;
    }

    public void setShutdown(String shutdown) {
        this.shutdown = shutdown;
    }

    public DriverTo useDriver() {
        String name = this.getType();
        for (DriverTo driver : this.getDrivers()) {
            if (driver.getName().equals(name)) {
                return driver;
            }
        }
        return null;

    }

}
