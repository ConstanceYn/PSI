import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MainServeur{

  public static void main(String[] args){
    Serveur S = new Serveur();

    int port = 1027;
    ServerSocket serveur = null;
    try {
      serveur = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Echec création socket");
    }
    int nbrConnections = 0;
    while(true){
      try {
        Socket connection = serveur.accept();
        System.out.println("connexion !");

        SocketRun connectRun = new SocketRun(connection, S);
        Thread t = new Thread(connectRun);
        t.start();
      } catch (IOException e) {
        System.err.println("Echec connection");
      }
    }
  }




}
