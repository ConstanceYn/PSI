import java.lang.String;
import java.net.*;
import java.util.Scanner;


public class Annonce {
    private String domaine;
    private String titre;
    private String descriptif;
    private float prix;
    private int id;
    // ip
    // utilisateur

    public Annonce(String d, String t, String des, float p, int i){
        domaine = d;
        titre = t;
        descriptif = des;
        prix = p;
        id = i;
    }


    // créer une annonce à partir de l'entrée utilisateur
    public Annonce(){
        Scanner inFromUser = new Scanner(System.in);

        System.out.print("domaine : ");
        String sentence = inFromUser.nextLine();
        domaine = sentence;


        System.out.print("titre : ");
        sentence = inFromUser.nextLine();
        titre = sentence;

        System.out.print("descriptif : ");
        sentence = inFromUser.nextLine();
        descriptif= sentence;

        System.out.print("prix : ");
        prix = inFromUser.nextFloat();

        id = -1;
        // l'id n'est pas encore set
    }

    // setter (et oui il faut les mettre aussi parce qu'on peut modifier une annonce)
    public void setDomaine(String s){
        domaine = s;
    }
    public void setTitre(String t){
        titre = t;
    }
    public void setDescriptif(String d){
        descriptif = d;
    }
    public void setPrix(Float p){
        prix = p;
    }
    public void setId(int i){
        id = i;
    }

    // ip + utilisateur


    // getters
    public String getDomaine(){
      return this.domaine;
    }

    public String getTitre(){
      return this.titre;
    }

    public String getDescriptif(){
      return this.descriptif;
    }

    public float getPrix(){
      return this.prix;
    }

    public int getId(){
      return this.id;
    }

        // ip + utilisateur


    // pour la commande send_domaine
    public boolean is_domaine(String d)
    {
        return (this.domaine.compareTo(d) == 0);
    }


    // quand un client envoie une nouvelle annonce au serveur
    public String Annonce_to_Client()
    {
        String str = new String();
        str = this.id+"\n"+this.domaine + "\n"+ this.titre + "\n"+ this.descriptif + "\n" + Float.toString(this.prix)+ "\n.\n";
        return str;
    }

    // quand le serveur envoie les annonces au client
    public String Annonce_to_Serveur()
    {
        String str = new String();
        str = this.domaine + "\n"+ this.titre + "\n"+ this.descriptif + "\n" + Float.toString(this.prix)+ "\n.\n";
        return str;
    }



}
