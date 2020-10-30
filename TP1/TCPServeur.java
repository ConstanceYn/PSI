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
    Compteur cmp = new Compteur();
    while(true){
      try {
        Socket connection = serveur.accept();
        System.out.println("connexion !");

        SocketRun connectRun = new SocketRun(connection, cmp);
        Thread t = new Thread(connectRun);
        t.start();

        //connection.close();
      } catch (IOException e) {
        System.err.println("Echec connection");
      }
    }
  }

}
