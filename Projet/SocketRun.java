import java.net.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketRun implements Runnable {
  private Socket connection;
  private Serveur serveur;
  private int token; // pour retenir le token du client qu'on traite sans le chercher à chaque fois
  private BufferedReader networkIn;
  private PrintWriter writer;

  public SocketRun(Socket connection, Serveur s){
    this.connection = connection;
    this.serveur = s;
    this.token = 0;
    try{
      this.writer = new PrintWriter(connection.getOutputStream());
      this.networkIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }catch(IOException e){

    }
  }


  public void run(){
    try {
      boolean continuer = true;

      String str = "Bonjour !! Bienvenue sur Good Duck";
      writer.println(str);
      writer.flush();


      String intro = "Entrez le nombre de la requete que vous voulez executer :\n";
      String un = "> 1 : Connection\n";
      String deux = "> 2 : Poster une annonce\n";
      String trois = "> 3 : Modifier une annonce \n";
      String quatre = "> 4 : Supprimer une annonce \n";
      String cinq = "> 5 : Afficher les domaines \n";
      String six = "> 6 : Afficher les annonces d'un domaine \n";
      String sept = "> 7 : Afficher ses annonces \n";
      String huit = "> 8 : deconnection \n";
      String cmd = "\n" +intro +un+ deux + trois + quatre + cinq + six + sept + huit + ".\n";

      writer.write(cmd);
      writer.flush();
      System.out.println("on a envoye les commandes");

      while(continuer){
        try {
          // on écoute le message du client
          System.out.println("test read please");

          String content = "";
          String msg = "";
          while(content != null && !content.equals(".")){
            content = networkIn.readLine();
            if (content != null && !content.equals("")){ // On ne prend pas en compte les lignes vides ou null :p
              msg += content + "\n";
            }
            //System.out.println("content" + content);
          }
          if (content != null){ // permet d'éviter de manipuler un msg null
            System.out.println("msg \n" + msg);
            continuer = parse(msg, serveur);
            System.out.println("sortie du parse");
            System.out.println();
          }

          // 10 secondes off
          Thread.sleep(1000);

        } catch (IOException e) {
          System.err.println("Echec connection read");
          e.printStackTrace();
          continuer = false;
        }
      }
      System.out.println("fin du client " + token);
    }catch(InterruptedException e){

    }
  }






  public boolean parse(String cmd, Serveur s) {
    Message message = Message.strToMessage(cmd);
    String commande = message.getType();

    Message reponse = null;
    switch(commande) {
      case "CONNECT":
      reponse = this.connect(message, s);
      break;
      case "DISCONNECT":
      User u = s.getUser(token);
      u.set_disconnect();
      System.out.println( u.getConnected());
      return false;
      case "POST_ANC":
      reponse = this.annonce(message, s);
      break;
      case "MAJ_ANC":
      reponse = this.maj_annonce(message, s);
      break;
      case "DELETE_ANC":
      reponse = this.delete(message, s);
      break;
      case "REQUEST_DOMAIN":
      reponse = this.req_dommain(message, s);
      break;
      case "REQUEST_ANC":
      reponse = this.req_annonce(message, s);
      break;
      case "REQUEST_OWN_ANC":
      reponse = this.req_own_annonce(message, s);
      break;
      case "REQUEST_IP":
      break;
      default :
      // UNKNOWN_REQUEST
      break ;
    }
    String rep = reponse.messageToStr();
    System.out.println(rep);
    System.out.println("on envoie la réponse");
    this.writer.println(rep);
    this.writer.flush();
    System.out.println("réponse envoyé");
    return true;

  }



  //
  // FONCTION POUR SE CONNECTER
  //
  public Message connect(Message msg, Serveur s)
  {
    String arg0 = msg.getArgs()[0];
    try {
      // if args[0] == int
      //alors déjà un user dans la BDD
      int i = Integer.parseInt(arg0);
      User u = s.getUser(i);
      if (u == null)
      return Message.connectKo();

      if (u.getConnected())
      return Message.connectKo();

      u.set_connect();
      u.setIp(connection.getInetAddress());
      this.token = u.getToken();
      return Message.connectOk();

    } catch (Exception e) { // le client a envoyé un nom
      // regarder si l'user éxiste deja :
      User u = s.getUser(arg0);
      if (u!=null){
        if (u.getConnected())
        return Message.connectKo();
        u.set_connect();
        u.setIp(connection.getInetAddress());
        this.token = token;
        return Message.connectOk();
      }
      // sinon
      System.out.println("nouvel utilisateur");
      u  = new User(arg0, connection.getInetAddress());
      int token = u.getToken();
      this.token = token;
      s.add_User(u);
      return Message.connectNewUserOk(Integer.toString(token));
    }

  }

  //
  //FONCTION POUR AJOUTER UNE ANNONCE AU SERVEUR
  //
  public Message annonce(Message msg, Serveur s)
  {
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    Annonce a = new Annonce(msg);
    if (a== null)
    {
      reponse = Message.postAncKo();
    }
    int id = s.get_nbAnn();
    a.setId(id);
    User u = s.getUser(token);
    a.setUser(u);
    s.add_Annonce(a);
    s.add_Domaine(a);
    System.out.println(a.Annonce_from_Serveur());
    reponse = Message.postAncOk(id);
    return reponse;

  }


  //
  //FONCTION POUR MODIFIER UNE ANNONCE
  //
  public Message maj_annonce(Message message, Serveur s){

    System.out.println("maj annonce peut être ? ");
    if (token == 0)
    {
      Message reponse = Message.notConnected();
      return reponse;
    }
    try {
      System.out.println("aaaaaaaaa");
      int num_ann = Integer.parseInt(message.getArgs()[0]);
      System.out.println("0");

      Annonce a = s.get_Ann().get(num_ann-1);

      if(!message.getArgs()[1].equals("null")){
        a.setDomaine(message.getArgs()[1]);
      }
      System.out.println("1");
      if(!message.getArgs()[2].equals("null")){
        a.setTitre(message.getArgs()[2]);
      }
      System.out.println("2");
      if(!message.getArgs()[3].equals("null")){
        a.setDescriptif(message.getArgs()[3]);
      }
      System.out.println("3");
      if(!message.getArgs()[4].equals("null")){
        try {
          float f = Float.parseFloat(message.getArgs()[4]);
          a.setPrix(f);
        }catch(Exception e){
          Message reponse = Message.majAncKo();
          return reponse;
        }
      }
      System.out.println("4");
      Message reponse = Message.majAncOk(num_ann);
      return  reponse;

    }catch(Exception e) {
      Message reponse = Message.majAncKo();
      return reponse;
    }

    // Message reponse = Message.majAncKo();
    // return reponse;

  }

  // FONCTION POUR SUPPRIMER UNE ANNONCE
  public Message delete(Message m, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    int id = Integer.parseInt(m.getArgs()[0]); // Id de l'annonce à détruire
    ArrayList<Annonce> annonces = s.get_Ann();
    for (int i = 0; i < annonces.size(); i++){
      Annonce a = annonces.get(i);
      if (a.getId() == id && a.getUser().getToken() == token){
        annonces.remove(i);
        return Message.deleteAncOk(id);
      }
    }
    return Message.deleteAncKo();
  }

  // FONCTION POUR ENVOYER LES DOMAINES
  public Message req_dommain(Message m, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    ArrayList<String> dom = s.get_Dom();
    String[] domaines = new String[dom.size()];
    domaines = dom.toArray(domaines);
    reponse = Message.sendDomainOk(domaines);
    return reponse;
  }


  //
  //FONCTION POUR ENVOYER TOUTES LES ANNONCES D'UN DOMAINE
  //
  public Message req_annonce(Message m, Serveur s)
  {
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    String domaine = m.getArgs()[0]; // domaine demandé
    ArrayList<Annonce> annonces = s.get_Ann();
    ArrayList<Annonce> rep = new ArrayList<Annonce>();
    for (int i = 0; i < annonces.size(); i++){
      Annonce a = annonces.get(i);
      if (a.getDomaine().equals(domaine)){
        rep.add(a);
      }
    }
    Annonce[] req = new Annonce[rep.size()];
    req = rep.toArray(req);
    reponse = Message.sendAncOk(req);
    return reponse;
  }

  public Message req_own_annonce(Message m, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    ArrayList<Annonce> annonces = s.get_Ann();
    ArrayList<Annonce> rep = new ArrayList<Annonce>();
    for (int i = 0; i < annonces.size(); i++){
      Annonce a = annonces.get(i);
      if (a.getUser().getToken() == token){
        rep.add(a);
      }
    }
    Annonce[] req = new Annonce[rep.size()];
    req = rep.toArray(req);
    reponse = Message.sendAncOk(req);
    return reponse;
  }

}
