package sample.Telemetry.browser;

import java.util.Map;

public class FireFoxBrowser extends GenericBrowser implements BrowserInfo{

   private final String name = "Firefox";

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Map<String, String> getUserBrowserInformation() {
      return null;
   }
}
