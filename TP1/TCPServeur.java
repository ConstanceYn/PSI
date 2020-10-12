import java.net.ServerSocket;
import java.io.IOException;

public class TCPServeur{

  public static void main(String[] args){
    private int port = 1027;
    private ServerSocket serveur = null;
    try {
      serveur = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Echec création socket");
    }
  }

}
