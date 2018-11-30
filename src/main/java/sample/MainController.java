package sample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sample.dao.DisplayableTableInfo;
import sample.dao.UploadableFile;
import sample.telemetry.Feed;

public class MainController implements Initializable {

   private long lastAddFileTime = 0;
   private final Feed f = Feed.getFeedInstance();

   /**
    * File cache that we use to keep the transformed files, uploaded by the user
    */
   private final Set<UploadableFile> _localFileCache = new HashSet<>();

   private final List<DisplayableTableInfo> displayableTableInfos = new ArrayList<>();

   private ObservableList<DisplayableTableInfo> fileNamesView =
         FXCollections.observableArrayList(displayableTableInfos);

   @FXML
   TableView<DisplayableTableInfo> tableView;
   @FXML
   TableColumn<DisplayableTableInfo, String> nameCol;
   @FXML
   TableColumn<DisplayableTableInfo, String> locationCol;
   @FXML
   TableColumn<DisplayableTableInfo, String> statusCol;
   @FXML
   TableColumn<DisplayableTableInfo, String> md5Col;
   @FXML
   Button addButton;
   @FXML
   Button removeButton;
   @FXML
   Button scanButton;

   private void initializeServerBackground() {
      NodeServer server = new NodeServer();
      server.deploy();
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      initializeServerBackground();
      generateTableColumns();
      printBasicInfo();
   }

   private void generateTableColumns(){
      nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));
      statusCol.setCellValueFactory(new PropertyValueFactory<>("isMalicious"));
      md5Col.setCellValueFactory(new PropertyValueFactory<>("md5"));
   }

   private void printBasicInfo() {
      System.out.println(f.getOs());
      System.out.println(f.getArch());
      System.out.println(f.getVers());
      System.out.println(f.getUserName());
      System.out.println(f.getRemoteIp());
      f.generateUsedPortSchema();
   }

   @FXML
   private void scanFileClick(ActionEvent actionEvent) {
      //if(hasDemoTimeElapsed()) {
         DisplayableTableInfo displayableTableInfo = tableView.getSelectionModel().getSelectedItem();
         String fileLocation = displayableTableInfo.getFileLocation();
         if(checkIfFileExistsInDir(fileLocation)) {
            for(UploadableFile uploadableFile : _localFileCache) {
               if(uploadableFile.getFile().getAbsolutePath().equals(fileLocation)) {
                  // interact with the api via the hash
                  lastAddFileTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
               }
               else {
                  AlertBuilder.createWarningAlert(AlertBuilder.tamperedFileTitle, AlertBuilder.tamperedFileText);
                  continue;
               }
            }
         }
         }

    //  else {
    //     AlertBuilder.createWarningAlert(AlertBuilder.trialTitle, AlertBuilder.trialText);
    //  }
  // }

   @FXML
   private void quitApplication(ActionEvent actionEvent) {
      // Maybe save current state?
      Platform.exit();
   }

   /**
    * Removes the file from view, but keeps it intact in the set for future checks.
    */
   @FXML
   private void removeFileClick(ActionEvent actionEvent) {
      DisplayableTableInfo tableInfo = tableView.getSelectionModel().getSelectedItem();
      tableView.getItems().remove(tableInfo);
   }

   @FXML
   private void addFileClick(ActionEvent actionEvent){
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Chose a file to scan");
         File userUploadedFile = fileChooser.showOpenDialog(null);
         if(userUploadedFile != null){
            if(isFileSizeAcceptable(userUploadedFile)){
               UploadableFile file = new UploadableFile(userUploadedFile);
               addFileToLocalFileCacheAndView(file);
            }
            else{
               AlertBuilder.createErrorAlert(AlertBuilder.fileSizeTitle,AlertBuilder.fileSizeWarning);
               addFileClick(actionEvent);
            }
         }
      else {
         AlertBuilder.createWarningAlert(AlertBuilder.trialTitle, AlertBuilder.trialText);
      }
   }

   /**
    * If the program has initialized for the first time (even if closed and reopened). Else,
    * the user needs to wait for 60 seconds to add file again
    * @return true if 60 seconds have passed since the las call
    */
   private boolean hasDemoTimeElapsed() {
      if(lastAddFileTime == 0){
         return true;
      }
      long currTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
      return lastAddFileTime + 60 >= currTime? false : true;
   }

   /**
    * Checks whether the file size is gt 30Mb.
    * @param f uploaded file
    * @return true if the size is lt 30 Mb
    */
   private boolean isFileSizeAcceptable(File f){
      return f.length() < 31457280 ? true : false;
   }

   /**
    * The method should:
    * 1. Set the uploadableFile in the local cache set
    * 2. Create DisplayableTableInfo object from the file
    * 3. Populate the view with information from displayableTableInfo object
    * @param uploadableFile the File that the user has uploaded
    */
   private void addFileToLocalFileCacheAndView(UploadableFile uploadableFile){
      if(_localFileCache.size() == 0) {
         _localFileCache.add(uploadableFile);
         addFileToView(uploadableFile);
      }
      else{
         for(UploadableFile cachedFile : _localFileCache) {
            if(uploadableFile.getMd5().equals(cachedFile.getMd5())) {
               AlertBuilder.createInfoAlert(AlertBuilder.duplicateFileTitle, AlertBuilder.duplicateFileText);
               break;
            }
            else {
               _localFileCache.add(uploadableFile);
               addFileToView(uploadableFile);
            }
         }
      }
   }

   private void addFileToView(UploadableFile uploadableFile) {
      DisplayableTableInfo displayableTableInfo = new DisplayableTableInfo(
            uploadableFile.getFile().getName(),
            uploadableFile.getFile().getAbsolutePath(),
            uploadableFile.getMd5(),   
            "Uploading...");
      displayableTableInfos.add(displayableTableInfo);
      fileNamesView.add(displayableTableInfo);
      tableView.setItems(fileNamesView);
   }

   /**
    * Checks if a certain absolute path contains the target file, and runs checks to verify the contents of the file
    * @param filePath the Absoulte filepath - meaning file included
    * @return true if a file exists and was not tampered with, false otherways
    */
   private boolean checkIfFileExistsInDir(String filePath) {
      File f = new File(filePath);
      if(f.exists()) {
         UploadableFile tempFile = new UploadableFile(f);
         for(UploadableFile file : _localFileCache) {
            if(!tempFile.getSha256().equals(file.getSha256())) {
               AlertBuilder.createWarningAlert(AlertBuilder.tamperedFileTitle, AlertBuilder.tamperedFileText);
            }
            else {
               return true;
            }
            return false;
         }
      }
      else
         AlertBuilder.createErrorAlert(AlertBuilder.fileNotFoundTitle, AlertBuilder.fileNotFoundText);
      return false;
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
