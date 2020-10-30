import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.*;
import java.io.*;


public class TCPClient{

  public static void main(String[] args)
  {
    Socket soc = null;
    try {
      soc = new Socket("localhost", 1027);
      System.out.println("Port de communication côté serveur : " + soc.getPort());


      BufferedReader networkIn = new BufferedReader( new InputStreamReader(soc.getInputStream()));
      String content = "";
      while(true){
        content = networkIn.readLine();
        System.out.println(content);
        content = "";

      }
    }
    catch (IOException e){
      e.printStackTrace();
    }
    finally
    {
      if(soc != null){
        try {
          soc.close();
        }
        catch (IOException e) {
          e.printStackTrace();
          soc = null;
        }
      }
    }

  }

}
