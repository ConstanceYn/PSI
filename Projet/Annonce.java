import java.lang.String;
import java.net.*;
import java.util.Scanner;


public class Annonce {
    private String domaine;
    private String titre;
    private String descriptif;
    private float prix;
    private int id;

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

    public void setId(int i)
    {
        id = i;
    }



    public String Annonce_to_String()
    {
        String str = new String();
        str = this.id+"\n"+this.domaine + "\n"+ this.titre + "\n"+ this.descriptif + "\n" + Float.toString(this.prix)+ "\n.\n";
        return str;
    }





}
