package sample.telemetry;

import org.apache.commons.lang3.SystemUtils;
import sample.telemetry.client.ProcessDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Feed {

   private static Feed feedInstance = null;
   private final ExecutorService executorService;

   /**
    * The map gives information about the used ports. If it contains an integer (port), the map will return true.
    */
   private ConcurrentMap<Integer, ProcessDetails> portUsageSchema = new ConcurrentHashMap<>();

   private Feed() {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executorService = executor;
   }

   public static Feed getFeedInstance() {
      if (feedInstance == null) {
         feedInstance = new Feed();
      }
      return feedInstance;
   }

   private String getProp(String s) {
      return System.getProperty(s);
   }

   public String getOs() {
      return getProp(TelemetryUtil.osName);
   }

   public String getVers() {
      return getProp(TelemetryUtil.osVersion);
   }

   public String getArch() {
      return getProp(TelemetryUtil.osArch);
   }

   public String getUserName() {
      return getProp(TelemetryUtil.userName);
   }

   /**
    * Method used to obtain the local IP of a used network interface. Could work for multiple interfaces, this example
    * uses only the last/first interface to obtain an address.
    * @return String version of the address: eg. 10.1.1.34
    */
   public String getLocalIp() {
      String address = "";
      if (getOs() != "Mac OS X") {
         DatagramSocket s = null;
         try {
            s = new DatagramSocket();
            s.connect(InetAddress.getByName("8.8.8.8"), 10002);
            address = s.getLocalAddress().getHostAddress();
         } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
         }
      } else {
         Set<String> addresses = new HashSet<>();
         Enumeration e = null;
         try {
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
               NetworkInterface n = (NetworkInterface) e.nextElement();
               Enumeration ee = n.getInetAddresses();
               while (ee.hasMoreElements()) {
                  InetAddress i = (InetAddress) ee.nextElement();
                  addresses.add(i.getHostAddress());
               }
               if (addresses.size() > 0) {
                  address = (String) addresses.toArray()[addresses.size() - 1];
               }
            }
         } catch (SocketException e1) {
            e1.printStackTrace();
         }
      }
      return address;
   }

   /**
    * Method used to obtain the public IP of a used network interface.
    * @return String version of the address: eg. 125.43.11.25
    */
   public String getRemoteIp() {
      String address = "";
      URL url = null;
      try {
         url = new URL("http://checkip.amazonaws.com/");
         BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
         address = br.readLine();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return address;
   }

   public void generateUsedPortSchema() {
      executorService.execute(new PortScanner());
      if(!portUsageSchema.isEmpty() && portUsageSchema != null) {
         for(Integer i: portUsageSchema.keySet()) {
            System.out.println(portUsageSchema.getOrDefault(i, null));
         }
      }
   }

   public class PortScanner implements Runnable{

      @Override
      public void run() {
         if(portUsageSchema == null || portUsageSchema.isEmpty()) {
            for(int port = 1; port <=65535; port++) {
               Socket s = null;
               try{
                  s = new Socket();
                  s.connect(new InetSocketAddress("localhost", port), 1);
                  s.close();
                  ProcessDetails processDetails = new ProcessDetails();
                  if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC_OSX) {
                     Process p = Runtime.getRuntime().exec("lsof -i :" + port + " | awk '{print $1}'");
                     BufferedReader readr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                     String processN = "";
                     while(readr.readLine() != null) {
                        processN +=  " " + readr;
                     }
                     processDetails.setProcessName(processN);
                  }
                  else if(SystemUtils.IS_OS_WINDOWS_7 || SystemUtils.IS_OS_WINDOWS_8 || SystemUtils.IS_OS_WINDOWS_10) {
                     // Continue with windows logc
                  }
                  portUsageSchema.put(port, processDetails);
               } catch (IOException e) {
                  // We've hit an empty port
               }
            }
         }
      }
   }


   /**
    * The method is used to check if the current OS is Windows, Linux or OSX
    * @return 1 if the OS is Windows, 2 if the OS is Linux, 3 if the OS is OSX, 0 if unknown
    */
   public int checkIfWinOrLin() {
      int result = 0;
      if(SystemUtils.IS_OS_WINDOWS_10 || SystemUtils.IS_OS_WINDOWS_8 || SystemUtils.IS_OS_WINDOWS_7) {
         result = 1;
      }
      else if(SystemUtils.IS_OS_LINUX) {
         result = 2;
      }
      else if(SystemUtils.IS_OS_MAC_OSX) {
         result = 3;
      }
      return result;
   }
}
