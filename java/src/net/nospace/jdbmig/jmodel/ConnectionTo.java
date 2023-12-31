package net.nospace.jdbmig.jmodel;

import java.util.ArrayList;
import java.util.List;

public class ConnectionTo {

    private String type;    // oracle/mysql/postgres/informix
    private List<String> initStrings; // Session init
    private String beforeExecute; // Execute if defined before Start 
    private String afterExecute;   // Execute after
    private String jdbcUrl; // jdbc:postgresql://host/db
    private String charset;
    private String user;
    private String password;

    public ConnectionTo() {
        beforeExecute = null;
        afterExecute = null;
        initStrings = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getInitStrings() {
        return initStrings;
    }

    public void setInitStrings(List<String> initStrings) {
        this.initStrings = initStrings;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getBeforeExecute() {
        return beforeExecute;
    }

    public void setBeforeExecute(String beforeExecute) {
        this.beforeExecute = beforeExecute;
    }

    public String getAfterExecute() {
        return afterExecute;
    }

    public void setAfterExecute(String afterExecute) {
        this.afterExecute = afterExecute;
    }

}
