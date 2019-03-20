package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import se.soprasteria.automatedtesting.webdriver.api.base.BaseClass;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;



// TODO: Make more flexibe to allow non-string values such as integers, arrays etc.
public class JsonData extends BaseClass {

    public Map getValue(String url, String key) {
        JSONObject jo = getJsonFromUrl(url);
        Map<String, String> jsonData = new HashMap<>();

        if (jo.length() > 0) {
            jsonData.put(key, jo.getString(key));
        } else logger.warn("Returned json data was empty!");
        return jsonData;
    }

    public Map getValues(String url, String[] keys) {
        JSONObject jo = getJsonFromUrl(url);
        Map<String, String> jsonData = new HashMap<>();

        if (jo.length() > 0) {
            for (String key: keys) {
                jsonData.put(key, jo.getString(key));
            }
        } else logger.warn("Returned json data was empty!");
        return jsonData;
    }

    private JSONObject getJsonFromUrl(String url) {
        try {
            JSONObject jo = (JSONObject) new JSONTokener(IOUtils.toString(new URL(url).openStream())).nextValue();
            return jo;
        }
        catch (Exception e) {
            logger.warn("Couldn't get json data\nerror: %s", e.getMessage());
        }
        return new JSONObject();
    }
}
