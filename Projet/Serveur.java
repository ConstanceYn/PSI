import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Serveur {

  private ArrayList<Annonce> annonces;
  private int nbAnn;
  private ArrayList<User> utilisateurs;
  private ArrayList<String> domaines;

  public Serveur(){
    annonces = new ArrayList<Annonce>();
    nbAnn = 1;
    utilisateurs = new ArrayList<User>();
    domaines = new ArrayList<String>();
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

  public void add_Domaine(Annonce a)
  {
    String dom = a.getDomaine();
    if (!domaines.contains(dom))
      domaines.add(dom);
  }

  public int get_nbAnn()
  {
    return this.nbAnn;
  }

  public ArrayList<Annonce> get_Ann()
  {
    return this.annonces;
  }

  public ArrayList<String> get_Dom()
  {
    return this.domaines;
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
