package net.nospace.jdbmig.jexport;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.nospace.jdbmig.jutil.JParseValue;
import net.nospace.jdbmig.jcommon.DynamicConnect;
import net.nospace.jdbmig.jmodel.Config;
import net.nospace.jdbmig.jmodel.TableTo;
import net.nospace.jdbmig.jutil.JsonUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.nospace.jdbmig.jmodel.FieldTo;

/**
 *
 */
public class JExport {

    private static final Logger LOG = Logger.getLogger("JExport");

    public static void execute(Config config) {
        List<String> tables = config.getTables();
        try {
            System.out.printf("Connecting to: %s: %s%n", config.getConnection().getType(), config.getConnection().getJdbcUrl());
            DynamicConnect connection = new DynamicConnect(config);
            for (String table : tables) {
                System.out.printf("Read table: %s%n", table);
                ResultSet resultSet = connection.executeQuery("SELECT * FROM " + table);
                TableTo result = resultSet2Table(resultSet, table, config.getFieldToLowerCase());
                resultSet.close();
                //String json = JsonUtil.getMapper().writeValueAsString(result);
                String json = normalizeJson(result);
                String fileName = config.getDataDir() + table + ".json";
                System.out.printf("Write table: %s to %s%n", table, fileName);
                saveTable(fileName, json);
            }
            connection.close();
        } catch (SQLException | JsonProcessingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static TableTo resultSet2Table(ResultSet rs, String name, Boolean forceLowerCase) throws SQLException, IOException {
        TableTo table = new TableTo(name);
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        long rowCount = 0;
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                int columnType = rsmd.getColumnType(i);
                String fieldName = rsmd.getColumnName(i);
                if (forceLowerCase) {
                    fieldName = fieldName.toLowerCase();
                }
                Object value = JParseValue.parse(i, rs, columnType);
                row.put(fieldName, value);
                if (rowCount == 0) {
                    table.getFields().add(new FieldTo(fieldName, columnType));
                }
            }
            rowCount++;
            rows.add(row);
        }
        table.setData(rows);
        return table;
    }

    private static void saveTable(String fileName, String data) throws IOException {
        OutputStream outputStream = new FileOutputStream(fileName);
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8")) {
            outputStreamWriter.write(data); // json is your data 
            outputStreamWriter.flush();
        }
    }

    private static String normalizeJson(TableTo table) throws JsonProcessingException, IOException {
        String name = table.getName();
        String fields = buildJsonFields(table.getFields());
        String data = JsonUtil.getMapper().writeValueAsString(table.getData());
        String format = "{%n\"name\": \"%s\",%n\"fields\": [%n%s%n],%n\"data\": %s%n}";
        String json = String.format(format, name, fields, data);
        return json.replaceAll("\\r", "");
    }

    private static String buildJsonFields(List<FieldTo> fields) {
        List<String> jFields = new ArrayList<>(); 
        for (FieldTo field : fields) {
            String t = "\t{\"name\": \"$name\",\t\"type\": $type,\t\"renameTo\": null,\t\"dflt\": null}";
            t = t.replace("$name", field.getName());
            t = t.replace("$type", field.getType().toString());
            jFields.add(t);
        }
        return String.join(",\n", jFields);
    }

}
