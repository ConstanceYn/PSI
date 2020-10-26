//import java.lang.String;

public class Annonce {
    int id;
    String domaine;
    String titre;
    String descriptif;
    float prix;

    public Annonce(int i, String d, String t, String des, float p){
        id = i;
        domaine = d;
        titre = t;
        descriptif = des;
        prix = p;
    }

    public String Annonce_to_String(Annonce a)
    {
        String str = new String();
        str = this.domaine + "\n"+ this.titre + "\n"+ this.descriptif + "\n" + Float.toString(this.prix)+ ".\n";
        return str;
    }



}
