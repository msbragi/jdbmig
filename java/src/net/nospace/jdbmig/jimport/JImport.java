package net.nospace.jdbmig.jimport;

import com.fasterxml.jackson.core.type.TypeReference;
import net.nospace.jdbmig.jcommon.DynamicConnect;
import net.nospace.jdbmig.jmodel.Config;
import net.nospace.jdbmig.jmodel.TableTo;
import net.nospace.jdbmig.jutil.JsonUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.nospace.jdbmig.jexecute.JExecute;
import net.nospace.jdbmig.jmodel.FieldTo;
import net.nospace.jdbmig.jutil.JCheckConfig;

/**
 *
 */
public class JImport {

    public static void execute(Config config) {
        List<String> tables = config.getTables();
        try {
            System.out.printf("Connecting to: %s: %s%n", config.getConnection().getType(), config.getConnection().getJdbcUrl());
            DynamicConnect connection = new DynamicConnect(config);
            for (String table : tables) {
                String tableName = config.getDataDir() + table + ".json";
                System.out.printf("Parsing file: %s%n", tableName);
                TableTo t = JsonUtil.getMapper().readValue(new File(tableName), new TypeReference<TableTo>() {
                });
                System.out.printf("Writing to table: %s%n", table);
                insertRecords(connection, t);
            }
            connection.close();
        } catch (IOException | SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertRecords(DynamicConnect conn, TableTo t) throws SQLException, UnsupportedEncodingException {
        String SQLInsert = buildStmt(t);
        List<Map<String, Object>> data = t.getData();
        for (Map<String, Object> row : data) {
            PreparedStatement pstmt = conn.getConnection().prepareStatement(SQLInsert);
            int fieldNum = 1;
            for (FieldTo field : t.getFields()) {
                setValue(pstmt, row, field, fieldNum++);
            }
            pstmt.executeUpdate();
            pstmt.close();
        }
    }

    private static void setValue(PreparedStatement pstmt, Map<String, Object> row, FieldTo field, int fieldNum) throws SQLException, UnsupportedEncodingException {
        Object obj = row.get(field.getName());
        if(obj == null && field.getDflt() != null) {
            obj = field.getDflt();
        }
        if (obj == null) {
            pstmt.setObject(fieldNum, null);
            return;
        }
        switch (field.getType()) {
            case java.sql.Types.LONGVARBINARY:
            case java.sql.Types.VARBINARY:
            case java.sql.Types.BINARY:
            case java.sql.Types.BLOB:
                byte[] decode = JsonUtil.parseBase64(obj.toString());
                pstmt.setBytes(fieldNum, decode);
                return;
            case java.sql.Types.TIMESTAMP:
                pstmt.setTimestamp(fieldNum, JsonUtil.stringToTimeStamp(obj.toString()));
                return;
            case java.sql.Types.CLOB:
            case java.sql.Types.NCLOB:
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.NVARCHAR:
            default:
                pstmt.setObject(fieldNum, obj);
        }
    }

    private static String buildStmt(TableTo table) {
        List<String> fields = new ArrayList<>();
        List<String> params = new ArrayList<>();
        for (FieldTo field : table.getFields()) {
            if(field.getRenameTo() != null) {
                fields.add(field.getRenameTo());
            } else { 
                fields.add(field.getName());
            }
            params.add("?");
        }
        String sql = "INSERT INTO " + table.getName() + "(" + String.join(",", fields) + ")";
        sql += " VALUES (" + String.join(",", params) + ")";
        return sql;
    }

}
