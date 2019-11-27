package se.soprasteria.automatedtesting.webdriver.helpers.utility.system;

import org.apache.commons.io.IOUtils;
import se.soprasteria.automatedtesting.webdriver.api.utility.Errors;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;


public class Resources {

    public static String moveResourceToTemp(String resource, String name) {
        try {
            File tempFile = File.createTempFile(name, "");
            tempFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tempFile);
            IOUtils.copy(getAsStream(resource), out);
            tempFile.setExecutable(true);
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not copy the resource to the temporary location, resource: " + resource);
        }

    }

    public static String getAbsolutePath(String resource) {
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        URL url = Resources.class.getClassLoader().getResource(resource);
        if (url == null) {
            dumpClasspath();
            String[] fixes = {
                    "Verify that the following resource exists: " + resource,
                    "Verify that configuration is correct"
            };

            throw new RuntimeException(Errors.getErrorMessage("Failed to find file/folder in classpath", fixes));
        }
        File file = new File(url.getFile());
        file.setExecutable(true);
        return file.getAbsolutePath();
    }


    public static InputStream getAsStream(String resource) {
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        InputStream stream = Resources.class.getResourceAsStream(resource);
        if (stream == null) {
            dumpClasspath();
            throw new RuntimeException("Failed to find file in classpath, " +
                    "verify that the following resource exists: " + resource);
        }
        return stream;
    }

    public static void dumpClasspath () {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        System.out.println("************ Current classpath *************");
        for(URL url: urls){
            System.out.println(url.getFile());
        }
        System.out.println("********************************************");
    }
}
