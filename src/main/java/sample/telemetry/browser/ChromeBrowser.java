package sample.telemetry.browser;

import java.util.Map;

public class ChromeBrowser extends GenericBrowser implements BrowserInfo {

   private final String name = "Chrome";
   private final String replacedPath = super.getBrowserProfileLocation(this).
         replaceAll(" ","\\ ");

   @Override
   public String getName() {
      return name;
   }

   @Override
   public void getUserBrowserInformation() {
   }
}
