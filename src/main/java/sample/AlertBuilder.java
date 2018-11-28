package sample;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class AlertBuilder {

   public static final String companyMail = "samplemain@my.company";
   public static final String companyWebsite = "www.samplewebsite.com";

   public static final String fileSizeTitle = "File too-large";
   public static final String fileSizeWarning = "The filesize exceeds the limitation of 30MB per file. " +
         "Consider not opening this file, because it may be malicious and your computer may get infected.";

   public static final String trialTitle = "Trial version";
   public static final String trialText = "You're using the demo version of the program. If you want to exceed the limitations," +
         "you can buy the full version by contacting " + companyMail + " or by visiting: " + companyWebsite;

   public static final String duplicateFileTitle = "Duplicate file found";
   public static final String duplicateFileText = "The file is already added to the queue. Please, double-check the added items";

   public static void createErrorAlert(String title, String information) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.setTitle(title);
      alert.setContentText(information);
      alert.showAndWait();
   }

   public static void createWarningAlert(String title, String information) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.setTitle(title);
      alert.setContentText(information);
      alert.showAndWait();
   }

   public static void createInfoAlert(String title, String information) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.setTitle(title);
      alert.setContentText(information);
      alert.showAndWait();
   }
}
