package net.nospace.jdbmig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nospace.jdbmig.jexport.JExport;
import net.nospace.jdbmig.jimport.JImport;
import net.nospace.jdbmig.jmodel.Config;
import net.nospace.jdbmig.jutil.JCheckConfig;
import net.nospace.jdbmig.jutil.JsonUtil;
import net.nospace.jdbmig.jutil.jManifest;
import java.io.File;
import java.io.IOException;
import net.nospace.jdbmig.jmodel.ConnectionTo;

public class JDBMig {

    private static String CONFIG_FILE = null;
    private static String DATA_DIR = null;
    private static String USE_CONN = null;
    private static Boolean IMPORT = null;
    private static final String COPYRIGHT = "JDBMig - (c) Marco Sbragi - m.sbragi@nospace.net";
    
    public static void main(String[] args) {
        if (!parseArgs(args)) {
            showHelp();
            System.exit(1);
            return;
        }
        Config config = loadConfig();
        if (config == null || !parseConfig(config)) {
            showHelp();
            //System.exit(1);
            return;
        }
        if (IMPORT) {
            JImport.execute(config);
        } else {
            if (config.getPrettyPrint()) {
                JsonUtil.getMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
            }
            JExport.execute(config);
        }
        System.exit(0);
    }

    private static Config loadConfig() {
        ObjectMapper mapper = JsonUtil.getMapper();
        try {
            Config config = mapper.readValue(new File(CONFIG_FILE), new TypeReference<Config>() {});
            if (DATA_DIR != null) {
                config.setDataDir(DATA_DIR);
            }
            if (USE_CONN != null) {
                config.setUseConn(USE_CONN);
            }
            return config;
        } catch (IOException ex) {
            System.out.println("Error config file: " + ex.getMessage());
            return null;
        }
    }

    private static Boolean parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--import":
                case "-i":
                    IMPORT = true;
                    break;
                case "--export":
                case "-x":
                    IMPORT = false;
                    break;
                case "--config":
                case "-c":
                    i++;
                    CONFIG_FILE = args[i];
                    break;
                case "--dataDir":
                case "-d":
                    i++;
                    DATA_DIR = args[i];
                    break;
                case "--useConn":
                case "-u":
                    i++;
                    USE_CONN = args[i];
                    break;
                default:
                    System.out.println("Unknown option ["+args[i]+"]\n");
                    return false;
            }
        }
        return !(IMPORT == null || CONFIG_FILE == null);
    }

    private static boolean parseConfig(Config config) {
        System.out.println("Checking config file...");
        if (config == null) {
            return false;
        }
        if(config.getUseConn() != null) {
            config.setConnection(checkUserConn(config.getUseConn()));
        }
        if (config.getConnection() == null) {
            System.out.println("You must specify a connection in config file\n");
            return false;
        }
        // Check for driver file
        if (!JCheckConfig.checkDriver(config)) {
            return false;
        }
        if (IMPORT && !JCheckConfig.checkImport(config)) {
            return false;
        }
        if (!IMPORT && !JCheckConfig.checkExport(config)) {
            return false;
        }
        return true;
    }

    private static void showHelp() {
        System.out.println();
        try {
            String copy = jManifest.getCopyRightString();
            System.out.println(copy);
        } catch (IOException ex) {
            System.out.println(COPYRIGHT);
        }
        System.out.println("Usage: java -jar JDBMig.jar --import|--export --config config_file_path [--dataDir YOUR_EXISTING_PATH]");
        System.out.println("--config|-c file:\tconfiguration file see config/config.json as example");
        System.out.println("--import|-i:\t\timport data from json file");
        System.out.println("--export|-x:\t\texport data to json file");
        System.out.println("--useConn|-u conn:\tthe connection to use (if not have standard [connection] defined or more than one)");
        System.out.println("\t\t\t(optional this option override \"connection\"  or \"useConn\" defined in [config].json)");
        System.out.println("--dataDir|-d path:\tthe directory where store json files or read from");
        System.out.println("\t\t\t(optional this option override the one defined in config.json)");
        System.out.println();
    }

    private static ConnectionTo checkUserConn(String useConn) {
        ObjectMapper mapper = JsonUtil.getMapper();
        try {
            JsonNode root = mapper.readTree(new File(CONFIG_FILE));
            if(root.has(useConn)) {
                ObjectNode conn = (ObjectNode) root.get(useConn);
                return mapper.readValue(conn.toString(), new TypeReference<ConnectionTo>() {});
            }
        } catch (IOException ex) {
            return null;
        }
        return null;
    }

}
