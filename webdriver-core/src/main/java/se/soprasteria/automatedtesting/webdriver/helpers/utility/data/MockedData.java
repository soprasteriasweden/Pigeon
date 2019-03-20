package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

import com.google.gson.*;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseTestConfig;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MockedData {

    private static Deque<Integer> availableServerPorts = new ArrayDeque<>();
    private static Map<String, Integer> busyServerPorts = new HashMap<>();
    private static String urlString;

    public static void initServerPorts(Logger logger){
        if(havePortsBeenProvided()) {
            int startPort = Integer.parseInt(BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.port.start"));
            int endPort = Integer.parseInt(BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.port.end"));
            for(int port = startPort; port <= endPort; port++){
                availableServerPorts.push(port);
            }
            logger.info("Ports " + startPort + "-" + endPort + " will be used to connect to the mocked data server");
        }
    }

    public static void releasePort(String testName){
        if(havePortsBeenProvided() && busyServerPorts.containsKey(testName)) {
            int availablePort = busyServerPorts.remove(testName);
            availableServerPorts.push(availablePort);
        }
    }

    public static void loadMockedData(Logger logger, String testName, String mockedDataCategory, String mockedDataName) throws Exception{
        updateJsonFile(logger, mockedDataCategory, mockedDataName);
        restartDataServer(logger, testName, mockedDataCategory);
    }

    private static void updateJsonFile(Logger logger, String mockedDataCategory, String mockedDataName) throws Exception{
        try {
            String jsonFilePath = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data." + mockedDataCategory);
            if(jsonFilePath == null) throw new NullPointerException("Found no matching category property for mocked data, use qaProperty \"mocked.data.\" to set a category");

            if(!new File(jsonFilePath).exists()) throw new NullPointerException("No json file could be found, verify that the correct path has been provided to qaProperty mocked.data." + mockedDataCategory);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(new FileReader(jsonFilePath)).getAsJsonObject();

            String jsonDataName = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.json.data_name");
            if(jsonDataName == null) throw new NullPointerException("Found no matching json data element name property, use qaProperty \"mocked.data.json.data_name\" to set a name for json data element");

            String mockedDataPath = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data." + mockedDataCategory + "." + mockedDataName);
            if(mockedDataPath == null) throw new NullPointerException("Found no matching mocked data property, use qaProperty \"mocked.data." + mockedDataCategory + ".\" to set mocked data");

            replaceValueInJsonFile(jsonObject, jsonDataName, mockedDataPath);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter jsonFile = new FileWriter(jsonFilePath);
            jsonFile.write(gson.toJson(jsonObject));
            jsonFile.close();
            String[] filePath = mockedDataPath.split("/");
            logger.info("Updated " + filePath[filePath.length - 1] + " to use the mocked data in " + mockedDataName);
        } catch (IOException e) {
            logger.error("Could not update the json-file with the mocked data: " + e.getMessage());
            throw e;
        }
    }

    private static void replaceValueInJsonFile(JsonObject jsonO, String fieldName, String newProperty) {
        String[] jsonPropertyPath = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.json.path").split(", ");
        JsonObject jsonObject = jsonO;
        if(jsonPropertyPath.length > 0) {
            for(int i = 0; i < jsonPropertyPath.length; i++) {
                jsonObject = jsonObject.getAsJsonObject(jsonPropertyPath[i]);
            }
        }
        jsonObject.addProperty(fieldName, newProperty);
    }

    private static void restartDataServer(Logger logger, String testName, String mockedDataCategory) throws Exception{
        try {
            String protocol = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.url.protocol");
            String domainName = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.url.domain_name");
            String path = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.url.path");
            if(availableServerPorts.size() < 1) throw new NullPointerException("No free ports, use qaProperty \"mocked.data.server.port.start\" and \"mocked.data.server.port.end\" to set usable ports");
            if(protocol == null || domainName == null) throw new NullPointerException("Must set both protocol and domain name for mocked data server, use qaProperty \"mocked.data.server.url.protocol\" and \"mocked.data.server.url.domain_name\"");
            int port = availableServerPorts.pop();
            busyServerPorts.put(testName, port);

            urlString = protocol + domainName + ":" + port + path;
            java.net.URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);

            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "text/plain");

            String postData = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data." + mockedDataCategory);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(System.getProperty("user.dir") + "/" + postData);
            out.close();
            String serverResponse = con.getResponseMessage();
            String[] filePath = BaseTestConfig.getInstance().getConfig().getProperty("mocked.data." + mockedDataCategory).split("/");
            if(serverResponse.equals("OK")) {
                logger.info("Server response: \"" + serverResponse + "\", server restarted with updated " + filePath[filePath.length - 1]);
            } else {
                logger.info("Server response: \"" + serverResponse + "\", server did not restart correctly with updated " + filePath[filePath.length - 1]);
            }
        } catch (IOException e) {
            logger.error("Could not push the mocked data to the remote server with url " + urlString);
            logger.error("Verify that the correct URL has been provided and the mocked data server is running");
            throw e;
        }

    }

    private static boolean havePortsBeenProvided(){
        return BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.port.start") != null && BaseTestConfig.getInstance().getConfig().getProperty("mocked.data.server.port.end") != null;
    }

}
