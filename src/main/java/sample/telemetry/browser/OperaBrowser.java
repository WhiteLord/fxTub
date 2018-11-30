package sample.telemetry.browser;

import java.util.Map;

public class OperaBrowser extends GenericBrowser implements BrowserInfo{

   private final String name = "Opera";
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
