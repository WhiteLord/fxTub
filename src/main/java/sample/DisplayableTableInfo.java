package sample;

public class DisplayableTableInfo {

   private String fileName;
   private String fileLocation;
   private boolean isMalicious;

   public DisplayableTableInfo(String name, String location, boolean isMalic){
      fileName = name;
      fileLocation = location;
      isMalicious = isMalic;
   }
}
