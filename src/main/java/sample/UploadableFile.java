package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;


public class UploadableFile {

   private final File _file;
   private final String md5;
   private final String sha256;
   private boolean isUploaded = false;
   private boolean isChecked = false;
   private boolean _isFileMalicious;
   private final String fileName;
   private final String fileLocation;

   public UploadableFile(final File file) {
      _file = file;
      md5 = getFileHash(file, "md5");
      sha256 = getFileHash(file, "sha256");
      fileName = file.getName();
      fileLocation = file.getAbsolutePath();
   }

   public String getMd5() {
      return md5;
   }

   public String getSha256() {
      return sha256;
   }

   public File getFile() {
      return _file;
   }

   public void setIsUploaded(boolean status) {
      isUploaded = status;
   }

   public boolean getIsUploaded() {
      return isUploaded;
   }

   public boolean isChecked() {
      return isChecked;
   }

   public void setChecked(final boolean checked) {
      isChecked = checked;
   }

   public boolean isFileMalicious() {
      return _isFileMalicious;
   }

   public void setFileMalicious(boolean isFileMalicious) {
      _isFileMalicious = isFileMalicious;
   }

   // The algo could be MD5 or SHA256
   private String getFileHash(final File file, final String algo) {
      String hash = "";
      if(file != null){
         try {
            final FileInputStream fis = new FileInputStream(file);
            if(algo.toLowerCase().equals("md5")) {
               hash = DigestUtils.md2Hex(fis);
            }
            else if (algo.toLowerCase().equals("sha256")) {
               hash = DigestUtils.sha256Hex(fis);
            }
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return hash;
   }
}
