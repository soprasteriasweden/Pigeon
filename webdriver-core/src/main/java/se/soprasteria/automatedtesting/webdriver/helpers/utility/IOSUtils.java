package se.soprasteria.automatedtesting.webdriver.helpers.utility;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import se.soprasteria.automatedtesting.webdriver.helpers.utility.data.Unzipper;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Taken and modified from:
 * @see <a href="https://github.com/saikrishna321/AppiumTestDistribution/issues/95">https://github.com/saikrishna321/AppiumTestDistribution/issues/95</a>
 */

public class IOSUtils {

    public static synchronized Map<String, String> getIPAInfo(String ipaFilePath) {
        Map<String, String> ipaInfo = new HashMap<>();
        Unzipper unzipper = new Unzipper();

        File ipaFile = new File(ipaFilePath);
        String ipaAbsPath = ipaFile.getAbsolutePath();
        String ipaDirectory = new File(ipaAbsPath).getParent();
//        String ipaFileNameWithoutExt = FilenameUtils.removeExtension(ipaFile.getName());

        // remove old Payload folder if existed
        if (new File(ipaDirectory + "/Payload").exists()) {
            try {
                FileUtils.deleteDirectory(new File(ipaDirectory + "/Payload"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // unzip ipa zip file
        try {
            unzipper.unzip(ipaAbsPath, ipaDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fetch app file name, after unzip ipa
        String appFileName = "";
        outter: for (File file : new File(ipaDirectory + "/Payload").listFiles()) {
            if (file.toString().endsWith(".app")) {
                appFileName = new File(file.toString()).getAbsolutePath();
                break outter;
            }
        }

        String plistFilePath = appFileName + "/Info.plist";

        // parse info.plist
        File plistFile = new File(plistFilePath);
        NSDictionary rootDict = null;
        try {
            rootDict = (NSDictionary) PropertyListParser.parse(plistFile);

            // get bundle id
            NSString parameter = (NSString) rootDict.objectForKey("CFBundleIdentifier");
            ipaInfo.put("CFBundleIdentifier", parameter.toString());

            // get bundle signature
            parameter = (NSString) rootDict.objectForKey("CFBundleSignature");
            ipaInfo.put("CFBundleSignature", parameter.toString());

            // get application name
            parameter = (NSString) rootDict.objectForKey("CFBundleName");
            ipaInfo.put("CFBundleName", parameter.toString());

            // get version
            parameter = (NSString) rootDict.objectForKey("CFBundleVersion");
            ipaInfo.put("CFBundleVersion", parameter.toString());

            // get short version
            parameter = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
            ipaInfo.put("CFBundleShortVersionString", parameter.toString());

            // get dict version
            parameter = (NSString) rootDict.objectForKey("CFBundleInfoDictionaryVersion");
            ipaInfo.put("CFBundleInfoDictionaryVersion", parameter.toString());

            // get bundle display name
            parameter = (NSString) rootDict.objectForKey("CFBundleDisplayName");
            ipaInfo.put("CFBundleDisplayName", parameter.toString());

            // get ios mini. version
            parameter = (NSString) rootDict.objectForKey("MinimumOSVersion");
            ipaInfo.put("MinimumOSVersion", parameter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyListFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // remove unzip folder
        try {
            FileUtils.deleteDirectory(new File(ipaDirectory + "/Payload"));
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return ipaInfo;
    }
}
