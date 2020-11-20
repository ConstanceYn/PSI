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

      while(continuer){
        try {
          // on écoute le message du client
          //System.out.println("test read please");

          String content = "";
          String msg = "";
          while(content != null && !content.equals(".")){
            content = networkIn.readLine();
            if (content != null && !content.equals("")){ // On ne prend pas en compte les lignes vides ou null :p
              msg += content + "\n";
            }
          }
          if (content != null){ // permet d'éviter de manipuler un msg null
            System.out.println("msg \n" + msg);
            continuer = parse(msg, serveur);
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
        if (u != null) {
          u.set_disconnect();
          System.out.println( u.getConnected());
          return false;
        }
        System.out.println("Deconnexion");
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
        reponse = this.req_ip(message, s);
        break;
      default :
        reponse = Message.unknownRequest();
      break ;
    }
    String rep = reponse.messageToStr();
    System.out.println(rep);
    System.out.println("Réponse envoyée :");
    this.writer.println(rep);
    this.writer.flush();
    return true;

  }



  //
  // FONCTION POUR SE CONNECTER
  //
  public Message connect(Message msg, Serveur s)
  {
    if(msg.getArgs().length != 1){
      return Message.connectKo();
    }
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
      return Message.connectOk(this.token, u.getUtilisateur());

    } catch (Exception e) { // le client a envoyé un nom
      // regarder si l'user éxiste deja :
      User u = s.getUser(arg0);
      if (u!=null){
        if (u.getConnected())
          return Message.connectKo();

        u.set_connect();
        u.setIp(connection.getInetAddress());
        this.token = u.getToken();
        return Message.connectOk(this.token, u.getUtilisateur());
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

    try {
      // on vérirfie le nombre d'argument
      if (msg.getArgs().length != 4){
        reponse = Message.postAncKo();
        return reponse;
      }
      // on vérifie que le prix est bien un prix
      Float prix = Float.parseFloat(msg.getArgs()[3]);
      if (prix <0){
        reponse = Message.postAncKo();
        return reponse;
      }

      Annonce a = new Annonce(msg);
      if (a== null){
        reponse = Message.postAncKo();
        return reponse;
      }

      int id = s.get_nbAnn();
      a.setId(id);
      User u = s.getUser(token);
      // on vérifie qu'on a bien un utilisateur
      if(u == null){
        reponse = Message.postAncKo();
        return reponse;
      }
      a.setUser(u);
      s.add_Annonce(a);
      s.add_Domaine(a);
      System.out.println(a.Annonce_from_Serveur());
      reponse = Message.postAncOk(id);
      return reponse;


    }catch(Exception e){
      reponse = Message.postAncKo();
      return reponse;
    }


  }


  //
  //FONCTION POUR MODIFIER UNE ANNONCE
  //
  public Message maj_annonce(Message message, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    try {
      // on vérirfie le nombre d'argument
      if (message.getArgs().length != 5){
        reponse = Message.majAncKo();
        return reponse;
      }
      int num_ann = Integer.parseInt(message.getArgs()[0]);

      Annonce a = s.get_Ann().get(num_ann-1);

      if(!message.getArgs()[1].equals("null")){
        a.setDomaine(message.getArgs()[1]);
      }

      if(!message.getArgs()[2].equals("null")){
        a.setTitre(message.getArgs()[2]);
      }

      if(!message.getArgs()[3].equals("null")){
        a.setDescriptif(message.getArgs()[3]);
      }

      if(!message.getArgs()[4].equals("null")){
        try {
          // on vérifie que le prix est un prix
          float f = Float.parseFloat(message.getArgs()[4]);
          if (f<0){
            reponse = Message.majAncKo();
            return reponse;
          }
          a.setPrix(f);
        }catch(Exception e){
          reponse = Message.majAncKo();
          return reponse;
        }
      }

      reponse = Message.majAncOk(num_ann);
      return  reponse;

    }catch(Exception e) {
      reponse = Message.majAncKo();
      return reponse;
    }
  }


  //
  // FONCTION POUR SUPPRIMER UNE ANNONCE
  //
  public Message delete(Message m, Serveur s){
    if (token == 0)
    {
      return Message.notConnected();
    }
    // on vérifie le nombre d'arguments
    if (m.getArgs().length !=1){
      return Message.deleteAncKo();
    }

    try {
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

    }catch(Exception e){
      // si args[0] n'est pas un entier
      return Message.deleteAncKo();
    }
  }


  //
  // FONCTION POUR ENVOYER LES DOMAINES
  //
  public Message req_dommain(Message m, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    if(m.getArgs().length !=0){
      return Message.sendDomainKo();
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
    if(m.getArgs().length != 1){
      return Message.sendAncKo();
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



  //
  //FONCTION POUR ENVOYER TOUTES LES ANNONCES DU CLIENT
  //
  public Message req_own_annonce(Message m, Serveur s){
    Message reponse = null;
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    if(m.getArgs().length !=0){
      return Message.sendAncKo();
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

  public Message req_ip(Message m, Serveur s){
    Message reponse = null;
    ArrayList<Annonce> annonces = s.get_Ann();
    if (token == 0)
    {
      reponse = Message.notConnected();
      return reponse;
    }
    try {

      int id = Integer.parseInt(m.getArgs()[0]);
      Annonce a = null;
      for (int i = 0; i < annonces.size(); i++){
        if (annonces.get(i).getId() == id) {
          a = annonces.get(i);
        }
      }
      if (a != null) {
        String ip = a.getUser().getIp().getHostAddress();
        String nom = a.getUser().getUtilisateur();
        reponse = Message.requestIpOk(ip, nom);
      } else {
        reponse = Message.requestIpKo();
      }
      return reponse;
    }catch(Exception e){
      // si args[0] n'est pas un int
      return Message.requestIpKo();
    }

  }

}
