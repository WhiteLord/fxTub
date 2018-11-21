package sample;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Feed {

   private static Feed feedInstance = null;

   private Feed() {
   }

   public static Feed getFeedInstance() {
      if (feedInstance == null) {
         feedInstance = new Feed();
      }
      return feedInstance;
   }

   private String getProp(String s) {
      return System.getProperty(s).toString();
   }

   public String getOs() {
      return getProp("os.name");
   }

   public String getVers() {
      return getProp("os.version");
   }

   public String getArch() {
      return getProp("os.arch");
   }

   public String getUserName() {
      return getProp("user.name");
   }

   public String getAddr() {
      String ad = "";
      if (getOs() != "Mac OS X") {
         DatagramSocket s = null;
         try {
            s = new DatagramSocket();
            s.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ad = s.getLocalAddress().getHostAddress();
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
                  ad = (String) addresses.toArray()[addresses.size() - 1];
               }
            }
         } catch (SocketException e1) {
            e1.printStackTrace();
         }
      }
      return ad;
   }
}
