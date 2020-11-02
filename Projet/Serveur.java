import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Serveur {

  ArrayList<Annonce> annonces;
  int nbAnn;
  ArrayList<User> utilisateurs;

  public Serveur(){
    annonces = new ArrayList<Annonce>();
    nbAnn = 1;
    utilisateurs = new ArrayList<User>();
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


  // rechercher un user par son token
  public User getUser(int token){
    for (int i=0; i< utilisateurs.size();i++ ) {
      if(utilisateurs.get(i).getToken() == token)
        return utilisateurs.get(i);
    }
    return null;
  }

  // rechercher un user par son nom
  public User getUser(String nom){
    for (int i=0; i<utilisateurs.size();i++ ) {
      if(utilisateurs.get(i).getUtilisateur().equals(nom))
        return utilisateurs.get(i);
    }
    return null;
  }


  public Annonce getAnnonce(int id){
      return annonces.get(id);
  }



}
