import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Serveur {

  ArrayList<Annonce> annonces;
  int nbAnn;
  ArrayList utilisateurs;

  public Serveur(){
    annonces = new ArrayList();
    nbAnn = 1;
    utilisateurs = new ArrayList();
  }

  public void add_Annonce(Annonce a)
  {
    annonces.add(a);
    nbAnn++;
  }

  public void add_User(User u)
  {
    utilisateurs.add(u);
  }

  // juste pour le test
  // public void add()
  // {
  //   nbAnn ++;
  // }

  public int get_nbAnn()
  {
    return this.nbAnn;
  }
  public ArrayList<Annonce> get_Ann()
  {
    return this.annonces;
  }

}
