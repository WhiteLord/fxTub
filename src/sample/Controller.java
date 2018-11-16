package sample;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Controller implements Initializable {

   final FileChooser fileChooser = new FileChooser();

   @FXML
   private final TextField _uploadTextField = new TextField();

   @FXML
   private final Button _browseButton = new Button();

   @FXML
   private final Button _scanButton = new Button();

   private File _uploadedFile;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      _browseButton.setOnAction(this::uploadClick);
      _scanButton.setOnAction(this::scanClick);
   }

   @FXML
   private void scanClick(ActionEvent actionEvent) {
      // check if file is uploaded
      // check if there is a string path
      if(_uploadedFile != null){
         processUploadedFile(_uploadedFile);
      }
      else {
         String textFieldFileLocation = getTextFieldContent(_uploadTextField);
         if(textFieldFileLocation != ""){
            if(checkIfFileExsists(textFieldFileLocation)){
               File processFile = new File(textFieldFileLocation);
               processUploadedFile(processFile);
            }
         }
      }
   }

   @FXML
   private void uploadClick(ActionEvent actionEvent) {
      fileChooser.setTitle("Select file to scan");
      _uploadedFile = fileChooser.showOpenDialog(null);
      String filePath = _uploadedFile.getAbsolutePath();
      System.out.println("The absolute path is: " + filePath);
   }

   private boolean checkIfFileExsists(String filePath){
      boolean fileExists = false;
      File tempFile = new File(filePath);
      if(tempFile.exists() && tempFile.isFile()){
         fileExists = true;
      }
      return fileExists;
   }

   private String getTextFieldContent(TextField textField){
      return textField.getText() == null ? "" : textField.getText();
   }

   private void processUploadedFile(File file){
      System.out.println("Inegrate with cuckoo here and wait for callback function. ");
      System.out.println("File location: " + file.getAbsolutePath());
   }
}
