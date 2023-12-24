package net.nospace.jdbmig.jutil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 */
public class JParseValue {

    public static Object parse(int i, ResultSet rs, int type) throws SQLException, IOException {
        Object obj = rs.getObject(i);
        if (obj == null) {
            return null;
        }

        String out;
        switch (type) {
            case java.sql.Types.CLOB:
                Clob clob = rs.getClob(i);
                out = clob.getSubString(1, (int) clob.length());
                return out;
            case java.sql.Types.BLOB:
                Blob blob = rs.getBlob(i);
                InputStream bs = blob.getBinaryStream();
                byte[] allBytes = readAllBytes(bs, (int) blob.length());
                bs.close();
                return allBytes;
            case java.sql.Types.TIMESTAMP:
                Timestamp ts = rs.getTimestamp(i);
                return JsonUtil.timeStampToString(ts);
            case java.sql.Types.NCLOB:
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.NVARCHAR:
            case java.sql.Types.LONGVARBINARY:
            case java.sql.Types.VARBINARY:
            default:
                return obj;
        }
    }

    public static byte[] readAllBytes(InputStream inputStream, int length) throws IOException {
        //inputStream.reset();
        byte[] bytes = new byte[length];
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        dataInputStream.readFully(bytes);
        dataInputStream.close();
        return bytes;
    }
}
