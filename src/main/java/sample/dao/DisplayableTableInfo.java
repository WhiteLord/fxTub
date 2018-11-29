package sample.dao;

import javafx.beans.property.SimpleStringProperty;

public class DisplayableTableInfo {

   private SimpleStringProperty fileName;
   private SimpleStringProperty fileLocation;
   private SimpleStringProperty isMalicious;

   public DisplayableTableInfo(){}

   public DisplayableTableInfo(String name, String location, String isMalic){
      fileName = new SimpleStringProperty(name);
      fileLocation = new SimpleStringProperty(location);
      isMalicious = new SimpleStringProperty("Processing...");
   }

   public String getFileName() {
      return fileName.get();
   }

   public SimpleStringProperty fileNameProperty() {
      return fileName;
   }

   public void setFileName(String fileName) {
      this.fileName.set(fileName);
   }

   public String getFileLocation() {
      return fileLocation.get();
   }

   public SimpleStringProperty fileLocationProperty() {
      return fileLocation;
   }

   public void setFileLocation(String fileLocation) {
      this.fileLocation.set(fileLocation);
   }

   public String getIsMalicious() {
      return isMalicious.get();
   }

   public SimpleStringProperty isMaliciousProperty() {
      return isMalicious;
   }

   public void setIsMalicious(String isMalicious) {
      this.isMalicious.set(isMalicious);
   }
}
