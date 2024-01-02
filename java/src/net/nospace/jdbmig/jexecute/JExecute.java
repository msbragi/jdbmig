package net.nospace.jdbmig.jexecute;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.nospace.jdbmig.jcommon.DynamicConnect;
import net.nospace.jdbmig.jmodel.Config;
import net.nospace.jdbmig.jutil.JStringUtils;

/**
 *
 * @author (c) Marco Sbragi
 */
public class JExecute {

    public static void before(Config config) {
        String script = config.getConnection().getBeforeExecute();
        if (script == null || script.trim().equals("")) {
            return;
        }
        System.out.printf("Execute [before] script: [%s]%n", script);
        try {
            List<String> statements = getStatements(script);
            execute(config, statements);
        } catch (IOException ex) {
            Logger.getLogger(JExecute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void after(Config config) {
        String script = config.getConnection().getAfterExecute();
        if (script == null || script.trim().equals("")) {
            return;
        }
        System.out.printf("Execute [after] script: [%s]%n", script);
        try {
            List<String> statements = getStatements(script);
            execute(config, statements);
        } catch (IOException ex) {
            Logger.getLogger(JExecute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void execute(Config config, List<String> statements) {
        try {
            System.out.printf("Connecting to [%s] %s%n", config.getConnection().getType(), config.getConnection().getJdbcUrl());
            DynamicConnect connection = new DynamicConnect(config);
            for (String statement : statements) {
                System.out.printf("Execute statement: %s%n", statement);
                connection.execute(statement);
            }
            connection.close();
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(JExecute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static List<String> getStatements(String bootstrap) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(bootstrap)));
        List<JStringUtils.Token> tokens = JStringUtils.tokenizeSql(content);
        List<String> statements = new ArrayList<>();
        List<String> statement = new ArrayList<>();
        for (JStringUtils.Token token : tokens) {
            if (token.text.equals(";")) {
                statements.add(String.join(" ", statement));
                statement.clear();
                continue;
            }
            statement.add(token.text);
        }
        return statements;
    }

}
