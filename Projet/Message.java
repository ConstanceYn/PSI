public class Message{
  // actuellement public mais ça changera surement
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

  // Regarde si String a la forme d'un message (au moins un type même si inconnu et finit par .)
  public static boolean isMessageShaped(String s){
    String[] split = s.split("\n");
    //return (split.length > 1 && split[split.length - 1] == "."); bug :(
    return (split.length > 1);
  }

  public String getType() {
    return this.type;
  }

  public String[] getArgs(){
    return this.args;
  }
  // Transforme une string message en un Message
  public static Message strToMessage(String s){
    if (isMessageShaped(s)){
      String[] split = s.split("\n");
      String type = split[0];
      String[] args = new String[split.length - 2];
      for (int i = 1; i < split.length - 1; i++){
        args[i-1] = split[i];
      }
      return new Message(type, args);
    } else {
      System.out.println("String not a Message");
      return new Message("", new String[0]);
    }
  }

  // Fonctions static créant différent type de message
  // Je sais pas si je les laisse là ou fait une class à part que de fonctions static

  public static Message connect(String utilisateur){ // version possible demandant d'entrer l'info à l'utilisateur ?
    String[] args = {utilisateur};
    return new Message("CONNECT", args);
  }

  public static Message connect(int token){
    String[] args = {Integer.toString(token)};
    return new Message("CONNECT", args);
  }

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


  // Juste un commentaire pour qu'on se rappel de modifier cette fonction
  //quand on aura mieux fait Annonce
  // La fonction suivante est peut-être plus interessante alors ^^
  public static Message postAnc(Annonce a){
    String[] args = {a.getDomaine(), a.getTitre(), a.getDescriptif(), Float.toString(a.getPrix())};
    return new Message("POST_ANC", args);
  }

  // Version demandant à l'utilisateur d'écrire les infos :)
  public static Message postAnc(){
    Annonce a = new Annonce();
    String[] args = {a.getDomaine(), a.getTitre(), a.getDescriptif(), Float.toString(a.getPrix())};
    return new Message("POST_ANC", args);
  }

  public static Message postAncOk(int id){
    String[] args = {Integer.toString(id)};
    return new Message("POST_ANC_OK", args);
  }

  public static Message postAncKo(){
    return new Message("POST_ANC_KO", new String[0]);
  }


  // Pareil que plus haut !!!
  //
  public static Message majAnc(Annonce a){ // on devra donner une version mise à jour de l'annonce en argument
    String[] args = {Integer.toString(a.getId()), a.getDomaine(), a.getTitre(), a.getDescriptif(), Float.toString(a.getPrix())};
    return new Message("MAJ_ANC", args);
  }

  public static Message majAncOk(int id){
    String[] args = {Integer.toString(id)};
    return new Message("MAJ_ANC_OK", args);
  }

  public static Message majAncKo(){
    return new Message("MAJ_ANC_KO", new String[0]);
  }

  public static Message deleteAnc(int id){
    String[] args = {Integer.toString(id)};
    return new Message("DELETE_ANC", args);
  }

  public static Message deleteAncOk(int id){
    String[] args = {Integer.toString(id)};
    return new Message("DELETE_ANC_OK", args);
  }

  public static Message deleteAncKo(){
    return new Message("DELETE_ANC_KO", new String[0]);
  }

  public static Message requestDomain(){
    return new Message("REQUEST_DOMAIN", new String[0]);
  }

  public static Message sendDomainOk(String[] domains){
    return new Message("SEND_DOMAIN_OK", domains);
  }

  public static Message sendDomainKo(){
    return new Message("SEND_DOMAIN_KO", new String[0]);
  }

  public static Message requestAnc(){
    return new Message("REQUEST_ANC", new String[0]);
  }

  // Peut-être faire une variante qui envoit une liste d'annonces qu'on met en forme pour le message ensuite
  public static Message sendAncOk(String[] annonces){
    return new Message("SEND_ANC_OK", annonces);
  }

  public static Message sendAncOk(Annonce[] annonces){
    int size_Ann = annonces.length;
    int taille = 5*size_Ann;
    String[] arg = new String [taille];
    for (int i=0;i< size_Ann; i++) {
      for (int j=0; j<5; j++) {
          arg[ (i*5) +j] = annonces[i].getArgs(j);
      }
    }
    //Message(String type, String[] args);
    return new Message("SEND_ANC_OK", arg);
  }

  public static Message sendAncKo(){
    return new Message("SEND_ANC_KO", new String[0]);
  }

  public static Message requestOwnAnc(){
    return new Message("REQUEST_OWN_ANC", new String[0]);
  }

  // Peut-être faire une variante qui envoit une liste d'annonces qu'on met en forme pour le message ensuite
  public static Message sendOwnAncOk(String[] annonces){
    return new Message("SEND_OWN_ANC_OK", annonces);
  }

  public static Message sendOwnAncKo(){
    return new Message("SEND_OWN_ANC_KO", new String[0]);
  }

  public static Message requestIp(){
    return new Message("REQUEST_IP", new String[0]);
  }

  public static Message sendIpOk(String ip, String user){
    String[] args = {ip, user};
    return new Message("SEND_IP_OK", args);
  }

  public static Message sendIpKo(){
    return new Message("SEND_IP_KO", new String[0]);
  }

  public static Message unknownRequest(){
    return new Message("UNKNOWN_REQUEST", new String[0]);
  }

  public static Message connectPair(String user){
    String[] args = {user};
    return new Message("CONNECT_PAIR", args);
  }

  public static Message connectPairOk(){
    return new Message("CONNECT_PAIR_OK", new String[0]);
  }

  public static Message connectPairRejected(){
    return new Message("CONNECT_PAIR_REJECTED", new String[0]);
  }

  public static Message connectPairKo(){
    return new Message("CONNECT_PAIR_KO", new String[0]);
  }

  public static Message disconnect_pair(){
    return new Message("DISCONNECT_PAIR", new String[0]);
  }

  public static Message sendMsg(String mes){
    String[] args = {mes};
    return new Message("SEND_MSG", args);
  }

  public static Message sendMsgOk(){
    return new Message("SEND_MSG_OK", new String[0]);
  }

  public static Message sendMsgKo(){
    return new Message("SEND_MSG_KO", new String[0]);
  }

}
