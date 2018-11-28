package sample.Telemetry.browser;

import java.util.Map;

public class OperaBrowser extends GenericBrowser implements BrowserInfo{

   private final String name = "Opera";

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Map<String, String> getUserBrowserInformation() {
      return null;
   }
}
