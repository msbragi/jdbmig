package net.nospace.jdbmig.jutil;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import net.nospace.jdbmig.JDBMig;

/**
 *
 * @author msbra
 */
public class jManifest {

    private static Attributes ATTRIBUTES = null;

    private static Attributes getManifest() throws IOException {
        if (ATTRIBUTES == null) {
            URLClassLoader cl = (URLClassLoader) JDBMig.class.getClassLoader();
            URL url = cl.findResource("META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(url.openStream());
            ATTRIBUTES = manifest.getMainAttributes();
        }
        return ATTRIBUTES;
    }

    public static String getCopyRightString() throws IOException {
        Attributes attrs = getManifest();
        String copy = attrs.getValue("AppName");
        copy += " " + attrs.getValue("Version");
        copy += " - " + attrs.getValue("Created-By");
        return copy;
    }

}
