package sample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
   private final TableView<DisplayableTableInfo> tableView= new TableView<>();
   @FXML
   private final TableColumn<DisplayableTableInfo, String> nameCol = new TableColumn<>();
   @FXML
   private final TableColumn<DisplayableTableInfo, String> locationCol = new TableColumn<>();
   @FXML
   private final TableColumn<DisplayableTableInfo, String> statusCol = new TableColumn<>();
   @FXML
   private final Button addButton = new Button();
   @FXML
   private final Button removeButton = new Button();
   @FXML
   private Button scanButton = new Button();

   private void initializeServerBackground() {
      NodeServer server = new NodeServer();
      server.deploy();
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      initializeServerBackground();
      generateTableColumns();
      printBasicInfo();
      fileNamesView.addListener(new ListChangeListener<DisplayableTableInfo>() {
         @Override
         public void onChanged(Change<? extends DisplayableTableInfo> c) {
            System.out.println("Change!");
         }
      });
      addButton.setOnAction(this::addFileClick);
      removeButton.setOnAction(this::removeFileClick);
      scanButton.setOnAction(this::scanFileClick);
   }

   private void generateTableColumns(){
      nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
      locationCol.setCellValueFactory(new PropertyValueFactory<>("fileLocation"));
      statusCol.setCellValueFactory(new PropertyValueFactory<>("isMalicious"));
      tableView.getColumns().add(nameCol);
      tableView.getColumns().add(locationCol);
      tableView.getColumns().add(statusCol);
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
            "Uploading...");
      displayableTableInfos.add(displayableTableInfo);
      fileNamesView.add(displayableTableInfo);
      tableView.setItems(fileNamesView);
      tableView.getColumns().addAll(nameCol, locationCol, statusCol);
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
