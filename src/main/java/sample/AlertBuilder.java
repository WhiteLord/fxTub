package sample;

import javafx.scene.control.Alert;

public class AlertBuilder {

   public static final String companyMail = "samplemain@my.company";
   public static final String companyWebsite = "www.samplewebsite.com";

   public static final String fileSizeTitle = "File too-large";
   public static final String fileSizeWarning = "The filesize exceeds the limitation of 30MB per file. " +
         "Consider not opening this file, because it may be malicious and your computer may get infected.";

   public static final String trialTitle = "This is a trial version of the program";
   public static final String trialText = "You're using the demo version of the program. If you want to exceed the limitations," +
         "you can buy the full version by contacting " + companyMail + " or by visiting: " + companyWebsite;

   public static void createErrorAlert(String title, String information) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setContentText(information);
      alert.showAndWait();
   }

   public static void createWarningAlert(String title, String information) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle(title);
      alert.setContentText(information);
      alert.showAndWait();
   }
}
