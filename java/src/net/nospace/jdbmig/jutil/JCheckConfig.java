package net.nospace.jdbmig.jutil;

import net.nospace.jdbmig.jcommon.DynamicConnect;
import net.nospace.jdbmig.jmodel.Config;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class JCheckConfig {

    public static boolean checkDriver(Config config) {
        // Check for driver
        if (config.useDriver() == null) {
            System.out.println("The driver: " + config.getType() + "  for the oonnection does not exist!!! Check config file");
            return false;
        }
        String jarFile = config.useDriver().getJarFile();
        if (!fileExist(jarFile)) {
            System.out.println("The jdbc driver: " + jarFile + "  does not exist!!! Check config file");
            return false;
        }
        return true;
    }

    public static boolean checkExport(Config config) {
        String dataDir = config.getDataDir();
        if (!dirExist(dataDir)) {
            System.out.println("Error: the directory for output " + dataDir + " does not exists.");
            return false;
        }
        return checkConnections(config);
    }

    public static boolean checkImport(Config config) {
        List<String> tables = config.getTables();
        List<String> jsonList = new ArrayList<>();
        String dataDir = config.getDataDir();
        if (!dirExist(dataDir)) {
            System.out.println("Error: the directory of json data " + dataDir + " does not exists.");
            return false;
        }
        // Check jsons
        for (String table : tables) {
            String path = dataDir + table + ".json";
            if (!fileExist(path)) {
                jsonList.add(table);
            }
        }
        if (!jsonList.isEmpty()) {
            System.out.println("Error: the listed json files for table(s) " + String.join(", ", jsonList) + " does not exist in " + dataDir);
            return false;
        }
        return checkConnections(config);
    }

    public static boolean checkConnections(Config config) {
        try {
            System.out.printf("Testing connections and tables for: %s: %s%n", config.getConnection().getType(), config.getConnection().getJdbcUrl());
            DynamicConnect connection = new DynamicConnect(config);
            for (String table : config.getTables()) {
                String sqlCheck = "Select 1 FROM " + table;
                ResultSet rs = connection.executeQuery(sqlCheck);
                rs.close();
            }
            connection.close();
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        return true;

    }

    public static boolean fileExist(String path) {
        File f = new File(path);
        return !(!f.exists() && !f.isDirectory());
    }

    public static boolean dirExist(String path) {
        File f = new File(path);
        return f.isDirectory();
    }

}
