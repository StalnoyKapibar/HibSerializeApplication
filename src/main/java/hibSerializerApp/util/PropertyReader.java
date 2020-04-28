package hibSerializerApp.util;

import java.io.*;
import java.util.Properties;

public class PropertyReader {
    public static Properties read(String path) {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(path)) {
            properties.load(is);
        } catch (FileNotFoundException e) {
            try (OutputStream os = new FileOutputStream(path)) {
                new File(path).createNewFile();
                properties.load(PropertyReader.class.getClassLoader().getResourceAsStream(path));
                properties.store(os, "");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void writeValue(String path, String property, String value) {
        Properties properties = new Properties();
        try (OutputStream os = new FileOutputStream(path)) {
            properties.load(PropertyReader.class.getClassLoader().getResourceAsStream(path));
            properties.setProperty(property, value);
            properties.store(os, "Change " + property + " to " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
