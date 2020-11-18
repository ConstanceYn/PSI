import java.net.*;
import java.io.*;

public class Connexion {
  private String nom;
  private InetAddress ip;
  private int port;


  public Connexion(String n, InetAddress i){
    nom = n;
    ip = i;
  }
}
