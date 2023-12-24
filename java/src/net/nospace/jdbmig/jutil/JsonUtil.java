package net.nospace.jdbmig.jutil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public abstract class JsonUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static ObjectMapper mapper = null;

    static class JByteArraySerializer extends StdSerializer<byte[]> {
        public JByteArraySerializer() {
            super(byte[].class);
        }

        @Override
        public void serialize(byte[] value, JsonGenerator jg, SerializerProvider sp) throws IOException {
            if (value != null && value.length > 0) {
                String b64 = Base64.getEncoder().encodeToString(value);
                jg.writeString(b64);
            } else {
                jg.writeString(Base64.getEncoder().encodeToString(value));
            }
        }
    }

    private static void setMapper() {
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        SimpleModule module = new SimpleModule("JByteArraySerializer");
        //module.addDeserializer(byte[].class, new ByteArrayDeserializer());
        module.addSerializer(byte[].class, new JByteArraySerializer());
        mapper.setDateFormat(df);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(module);
    }

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            setMapper();
        }
        return mapper;
    }

    public static String timeStampToString(Timestamp ts) {
        if (ts == null) {
            return null;
        }
        Date date = new Date();
        date.setTime(ts.getTime());
        String strDate = new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
        return strDate;
    }

    public static Timestamp stringToTimeStamp(String value) {
        try {
            DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = formatter.parse(value);
            return new Timestamp(date.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            return null;
        }
    }

    public static byte[] parseBase64(String value, String charset) {
        try {
            if(charset == null) {
                return Base64.getDecoder().decode(value.getBytes());
            }
            return Base64.getDecoder().decode(value.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            return Base64.getDecoder().decode(value.getBytes());
        }
    }

    public static byte[] parseBase64(String value) {
        return parseBase64(value, null);
    }

}
