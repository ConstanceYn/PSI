import java.net.*;
import java.io.*;

public class TCPServeur{

  public static void main(String[] args){
    int port = 1027;
    ServerSocket serveur = null;
    try {
      serveur = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Echec création socket");
    }
    while(true){
      try {
        Socket connection = serveur.accept();
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream( ));
        out.write("Connecté:" +connection+"\r\n");
        connection.close();
      } catch (IOException e) {
        System.err.println("Echec connection");
      }
    }
  }

}
