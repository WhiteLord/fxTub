package sample;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sample.Telemetry.Feed;

public class MainController implements Initializable {

   private long lastAddFileTime = 0;
   private final Feed f = Feed.getFeedInstance();

   // File cache we use to keep the uploaded files and run initial checks
   private final Set<UploadableFile> _localFileCache = new HashSet<>();

   @FXML
   private final TableView<UploadableFile> tableView= new TableView<>();
   @FXML
   private final TableColumn<UploadableFile, String> nameCol = new TableColumn<>();
   @FXML
   private final TableColumn<UploadableFile, String>locationCol = new TableColumn<>();
   @FXML
   private final TableColumn<UploadableFile, String>statusCol = new TableColumn<>();
   @FXML
   private final Button addButton = new Button();
   @FXML
   private final Button removeButton = new Button();
   @FXML
   private final Button scanButton = new Button();

   private ObservableList<UploadableFile> fileNamesView = null;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      File f = new File("/Users/ghristovspas/Desktop/commands.txt");
      UploadableFile file = new UploadableFile(f);
      fileNamesView = FXCollections.observableArrayList();
      fileNamesView.add(file);
      generateTableColumns();
      tableView.getColumns().addAll(nameCol, locationCol, statusCol);
      printBasicInfo();
      addButton.setOnAction(this::addFileClick);
      removeButton.setOnAction(this::removeFileClick);
      scanButton.setOnAction(this::scanFileClick);
   }

   private void generateTableColumns(){
      nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));
      statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
      tableView.setItems(fileNamesView);
   }

   private void printBasicInfo() {
      System.out.println(f.getOs());
      System.out.println(f.getArch());
      System.out.println(f.getVers());
      System.out.println(f.getUserName());
      System.out.println(f.getAddr());
   }

   @FXML
   private void scanFileClick(ActionEvent actionEvent) {
   }

   @FXML
   private void quitApplication(ActionEvent actionEvent) {
      // Maybe save current state?
      Platform.exit();
   }

   @FXML
   private void removeFileClick(ActionEvent actionEvent) {
   }

   @FXML
   private void addFileClick(ActionEvent actionEvent){
      if(hasTimeElapsed()){
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Chose a file to scan");
         File userUploadedFile = fileChooser.showOpenDialog(null);
         if(userUploadedFile != null){
            if(isFileSizeAcceptable(userUploadedFile)){
               lastAddFileTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
               UploadableFile file = new UploadableFile(userUploadedFile);
               addFileToLocalFileCacheAndView(file);
               System.out.println(userUploadedFile.getName());
            }
            else{
               AlertBuilder.createErrorAlert(AlertBuilder.fileSizeTitle,AlertBuilder.fileSizeWarning);
               addFileClick(actionEvent);
            }
         }
      }
      else {
         AlertBuilder.createWarningAlert(AlertBuilder.trialTitle, AlertBuilder.trialText);
      }
   }

   /**
    * If the program has initialized for the first time (even if closed and reopened). Else,
    * the user needs to wait for 60 seconds to add file again
    * @return
    */
   private boolean hasTimeElapsed() {
      if(lastAddFileTime == 0){
         return true;
      }
      long currTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
      return lastAddFileTime + 60 >= currTime? false : true;
   }

   private boolean isFileSizeAcceptable(File f){
      return f.length() < 31457280 ? true : false;
   }

   private void addFileToLocalFileCacheAndView(UploadableFile file){
      if(_localFileCache.size() == 0) {
         _localFileCache.add(file);
         addFileToView(file);
      }
      else{
         for(UploadableFile cachedFile : _localFileCache) {
            if(file.getMd5().equals(cachedFile.getMd5())) {
               AlertBuilder.createInfoAlert(AlertBuilder.duplicateFileTitle, AlertBuilder.duplicateFileText);
            }
            else {
               _localFileCache.add(file);
               addFileToView(file);
            }
         }
      }

   }

   private void addFileToView(UploadableFile file){
      fileNamesView.add(file);
      tableView.getItems().add(file);
      for(UploadableFile f : tableView.getItems()) {
         System.out.println("Files's name is : " + f.getFile().getName());
      }
   }

   private void removeFileFromView(File file){
      // Removes the file from view. Keep is in local cache.
   }
/*
   @FXML
   private void addFileClick(ActionEvent actionEvent) {
      ObservableList<String> addedFileNamesByButton = FXCollections.observableArrayList();
      fileChooser.setTitle("Select a file to scan");
      _uploadedFile = fileChooser.showOpenDialog(null);
      if(_uploadedFile != null){
         String filePath = _uploadedFile.getAbsolutePath();
         String fileName = _uploadedFile.getName();
         addedFileNamesByButton.add(fileName);
         System.out.println(fileName);
         // Add items to an object of some sort
         // Create ui, populate it
      }
   }
   */

}
