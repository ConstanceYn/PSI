import java.net.*;
import java.io.*;

public class Connexion {
  private String nom;
  private InetAddress ip;


  public Connexion(String n, InetAddress i){
    nom = n;
    ip = i;
  }
}
