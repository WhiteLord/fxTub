package sample.telemetry.browser;

import java.util.Map;

public class ChromeBrowser extends GenericBrowser implements BrowserInfo {

   private final String name = "Chrome";

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Map<String, String> getUserBrowserInformation() {
      return null;
   }
}
