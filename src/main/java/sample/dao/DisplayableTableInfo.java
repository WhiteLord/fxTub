package sample.dao;

import javafx.beans.property.SimpleStringProperty;

public class DisplayableTableInfo {

   private SimpleStringProperty fileName;
   private SimpleStringProperty fileLocation;
   private SimpleStringProperty isMalicious;
   private SimpleStringProperty md5;

   public DisplayableTableInfo(){}

   public DisplayableTableInfo(String name, String location, String md5Str, String isMalic){
      fileName = new SimpleStringProperty(name);
      fileLocation = new SimpleStringProperty(location);
      md5 = new SimpleStringProperty(md5Str);
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

    public String getMd5() {
        return md5.get();
    }

    public SimpleStringProperty md5Property() {
        return md5;
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
