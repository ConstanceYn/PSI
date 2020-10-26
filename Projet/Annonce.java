//import java.lang.String;

public class Annonce {
    String domaine;
    String titre;
    String descriptif;
    int prix;

    public Annonce(String d, String t, String des, int p){
        domaine = d;
        titre = t;
        descriptif = des;
        prix = p;
    }

    public String Annonce_to_String(Annonce a)
    {
        String str = new String();
        str = this.domaine + "\n"+ this.titre + "\n"+ this.descriptif + "\n" + this.prix.toString()+ ".\n";
        return str;
    }





}
