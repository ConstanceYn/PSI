import java.net.*;
import java.io.*;

public class Connexion {
  private String nom;
  private InetAddress ip;
  private int port;


  public Connexion(String n, InetAddress i, int p){
    nom = n;
    ip = i;
    port = p;
  }

  public String getNom(){
    return nom;
  }
}
