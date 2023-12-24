package net.nospace.jdbmig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.nospace.jdbmig.jexport.JExport;
import net.nospace.jdbmig.jimport.JImport;
import net.nospace.jdbmig.jmodel.Config;
import net.nospace.jdbmig.jutil.JCheckConfig;
import net.nospace.jdbmig.jutil.JsonUtil;
import java.io.File;
import java.io.IOException;

public class JDBMig {

    private static String CONFIG_FILE = null;
    private static String DATA_DIR = null;
    private static Boolean IMPORT = null;

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
            Config config = mapper.readValue(new File(CONFIG_FILE), new TypeReference<Config>() {
            });
            if (DATA_DIR != null) {
                config.setDataDir(DATA_DIR);
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
            if (arg.equals("--import") || arg.equals("-i")) {
                IMPORT = true;
            } else if (arg.equals("--export") || arg.equals("-x")) {
                IMPORT = false;
            } else if (arg.equals("--config") || arg.equals("-c")) {
                i++;
                CONFIG_FILE = args[i];
            } else if (arg.equals("--dataDir") || arg.equals("-d")) {
                i++;
                DATA_DIR = args[i];
            }
        }
        if (IMPORT == null || CONFIG_FILE == null) {
            return false;
        }
        return true;
    }

    private static boolean parseConfig(Config config) {
        System.out.println("Checking config file...");
        if (config == null) {
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
        System.out.println("\nUsage: jdbcUtil --import|--export --config config_file_path [--dataDir YOUR_EXISTING_PATH]");
        System.out.println("--import|-i:\t\timport data from json file");
        System.out.println("--export|-x:\t\texport data to json file");
        System.out.println("--config|-c file:\tconfiguration file see config/config.json as example");
        System.out.println("--dataDir|-d path:\tthe directory where store json files or read from");
        System.out.println("\t\t\t(optional this option override the one defined in config.json)");
    }

}
