public class Message{
  String type;
  String[] args;

  // Constructeur
  public Message(String type, String[] args){
    this.type = type;
    this.args = args;
  }

  // Transforme Message en la forme utilisée pour l'envoyer
  public String messageToStr(){
    String s = type + "\n";
    for (int i = 0; i < args.length; i++){
      s += args[i] + "\n";
    }
    s += ".\n";
    return s;
  }

  // Fonctions static créant différent type de message
  // Je sais pas si je les laisse là ou fait une class à part que de fonctions static

  public static Message connectOk(){
    return new Message("CONNECT_OK", new String[0]);
  }

  public static Message connectNewUserOk(String token){
    String[] args = {token};
    return new Message("CONNECT_NEW_USER_OK", args);
  }

  public static Message connectNewUserKo(){
    return new Message("CONNECT_NEW_USER_KO", new String[0]);
  }

  public static Message connectKo(){
    return new Message("CONNECT_KO", new String[0]);
  }

  public static Message notConnected(){
    return new Message("NOT_CONNECTED", new String[0]);
  }

  public static Message disconnect(){
    return new Message("DISCONNECT", new String[0]);
  }

  public static Message postAnc(Annonce a){
    String[] args = {a.domaine, a.titre, a.descriptif, Float.toString(a.prix)};
    return new Message("POST_ANC", args);
  }

  public static Message postAncOk(int id){
    String[] args = {Integer.toString(id)};
    return new Message("POST_ANC_OK", args);
  }

  public static Message postAncKo(){
    return new Message("POST_ANC_KO", new String[0]);
  }

  public static Message majAnc(Annonce a){ // on devra donner une version mise à jour de l'annonce en argument
    String[] args = {Integer.toString(a.id), a.domaine, a.titre, a.descriptif, Float.toString(a.prix)};
    return new Message("MAJ_ANC", args);
  }

  public static Message majAncOk(int id){
    String[] args = {Integer.toString(id)};
    return new Message("MAJ_ANC_OK", args);
  }

  public static Message postAncKo(){
    return new Message("MAJ_ANC_KO", new String[0]);
  }

}
