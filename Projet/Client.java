import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.sql.Timestamp;
import java.net.*;
import java.io.*;


public class Client{

  public static void main(String[] args)
  {
    Socket soc = null;
    DatagramSocket socUDP = null;
    try {
      String utilisateur = "";
      //"192.168.1.6"
      soc = new Socket("psi.maiste.fr", 1027);
      socUDP = new DatagramSocket(7201);

      System.out.println("Port de communication côté serveur : " + soc.getPort());
      System.out.println(soc.getInetAddress().toString());

      ClientUDP convRun = new ClientUDP(socUDP);
      Thread t = new Thread(convRun);
      t.start();
      // on ouvre la socket UDP après le thread pour que les deux soit sur le même port



      PrintWriter writer = null; // pour écrire au serveur
      BufferedReader networkIn = new BufferedReader( new InputStreamReader(soc.getInputStream())); // pour lire le serveur
      // pour lire l'entrée user
      BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

      // message de bienvenue du serveur
      String msg = "Bonjour !! Bienvenue sur Good Duck";
      System.out.println(msg);


      // liste des requetes possibles :

      String content = "";
      writer = new PrintWriter(soc.getOutputStream());

      // on envoie des requetes au serveur

      String intro = "Entrez le nombre de la requete que vous voulez executer :\n";
      String un = "> 1 : Connexion\n";
      String deux = "> 2 : Poster une annonce\n";
      String trois = "> 3 : Modifier une annonce \n";
      String quatre = "> 4 : Supprimer une annonce \n";
      String cinq = "> 5 : Afficher les domaines \n";
      String six = "> 6 : Afficher les annonces d'un domaine \n";
      String sept = "> 7 : Afficher ses annonces \n";
      String huit = "> 8 : Messagerie \n";
      String neuf = "> 9 : Deconnexion \n";
      String requetes = "\n" +intro +un+ deux + trois + quatre + cinq + six + sept + huit + neuf + ".\n";

      System.out.println(requetes);
      boolean continuer = true;
      while(continuer)
      {
        //System.out.println(requetes);
        int action2 = -1;
        int action = userIn.read()-'0';
        String message = null;

        switch(action) {
          case 1: // Connection
            System.out.println("Entrez le nom d'utilisateur ou le token : ");
            userIn.readLine();
            utilisateur = userIn.readLine();
            message = Message.connect(utilisateur).messageToStr();
            break;

          case 2: // poster une annonce
            message = Message.postAnc().messageToStr();
            System.out.println("message \n" + message + "fin message");

            break;
          case 3: //modifier une annonce
            String str = null;
            System.out.println("Quelle annonce voulez vos modifier ? (entrer l'identifiant )");
            userIn.readLine();
            message = "MAJ_ANC\n";
            message += userIn.readLine() + "\n";

            System.out.println("Si vous ne voulez pas modifier un critère appuyez juste sur entrer");
            System.out.print("nouveau domaine : ");
            str = userIn.readLine();
            if(str.equals(""))
              str = "null";
            message += str + "\n";

            System.out.print("nouveau titre : ");
            str = userIn.readLine();
            if(str.equals(""))
              str = "null";
            message += str + "\n";

            System.out.print("nouvelle description : ");
            str = userIn.readLine();
            if(str.equals(""))
              str = "null";
            message += str + "\n";

            System.out.print("nouveau prix : ");
            str = userIn.readLine();
            if(str.equals(""))
              str = "null";
            message += str + "\n.\n";

            break;
          case 4: // Supprimer une annonce
            System.out.println("Id de l'annonce que vous souaitez retirer ? : ");
            message = userIn.readLine();
            message = Message.deleteAnc(userIn.readLine()).messageToStr();
            break;
          case 5: // Afficher les domaines
            message = Message.requestDomain().messageToStr();
            break;
          case 6: // Afficher les annonces d'un domaine
            System.out.println("Quel domaine voulez vous ? : ");
            message = userIn.readLine();
            message = Message.requestAnc(userIn.readLine()).messageToStr();
            break;
          case 7: // Afficher ses annones
            message = Message.requestOwnAnc().messageToStr();
            break;
          case 8: // Demander IP et lancer discussion ?
            String un1 = "> 1 : Contacter un annonceur\n";
            String de2 = "> 2 : Lire ses messages\n";
            String tr3 = "> 3 : Envoyer un message\n";
            System.out.println(intro + un1 + de2 + tr3);
            userIn.readLine();
            action2 = userIn.read()-'0';
            switch(action2) {
              case 1:
                System.out.println("Indiquer l'id de l'annonce qui vous interesse : ");
                message = userIn.readLine();
                message = Message.requestIp(userIn.readLine()).messageToStr();
                break;
              case 2:
                System.out.println("Indiquer le nom de la personne dont vous souhaiter lire les messages : ");
                userIn.readLine();
                String name = userIn.readLine();
                System.out.println(name);
                if (convRun.isContact(name)){
                  readMsg(name);
                } else {
                  System.out.println("Ce nom ne figure pas dans vos contacts.");
                }
                action = -1;
                System.out.println(requetes);
                break;
              case 3:
                System.out.println("Indiquer le nom de la personne à qui vous souhaiter parler : ");
                userIn.readLine();
                String nom = userIn.readLine();
                System.out.println(nom);
                InetAddress ia = convRun.getIp(nom);
                if (ia != null) {
                  System.out.println("Rédigez votre message à " + nom + " :");
                  sendMsg(utilisateur, userIn.readLine(), ia, socUDP);
                } else {
                  System.out.println("Ce nom ne figure pas dans vos contacts.");
                }
                action = -1;
                System.out.println(requetes);
                break;
              default :
                action2 = -1;
                //System.out.println("Action inconnue");
                break;
              }
            break;
          case 9: // Deconnexion
            continuer = false;
            message = Message.disconnect().messageToStr();
            break;

          default :
            action = -1;
            action2 = -1;
            //System.out.println("Action inconnue");
            break ;
        }
        if (action != -1)
        {
          writer.println(message);
          writer.flush();

          if (action != 9 && action2 < 2)
          {
            //System.out.println("sorti du switch : on attend la réponse");
            content = "";
            String reponse = "";
            while( content != null && !content.equals(".")){
              content = networkIn.readLine();
              if (content != null && !content.equals("")){
                reponse += content + "\n";
              }
            }
            System.out.println("reponse du serveur : ");
            System.out.println(reponse);
            if (action == 1){
              Message rep = Message.strToMessage(reponse);
              if (rep.getArgs().length > 1) {
                utilisateur = rep.getArgs()[1];
              }
            }
            if (action == 8 && action2 == 1){
              if (! reponse.equals("REQUEST_IP_KO\n.\n")){
                Message rep = Message.strToMessage(reponse);
                System.out.println("Rédigez votre message à " + rep.getArgs()[1] + " :");
                sendMsg(utilisateur, userIn.readLine(), InetAddress.getByName(rep.getArgs()[0]), socUDP);

              }
            }
            System.out.println();

            System.out.println(requetes);
          }
        }

      }
      // close
      System.out.println("on stop avec le serveur ");
      convRun.stop();
      socUDP.close();
    }
    catch (UnknownHostException e){
      e.printStackTrace();
    }
    catch (IOException e){
      e.printStackTrace();
    }
    finally
    {
      if(soc != null){
        try {
          soc.close();
          if(socUDP != null){
            socUDP.close();
          }
        }
        catch (IOException e) {
          e.printStackTrace();
          soc = null;
        }
      }
    }

  }

  public static void sendMsg(String user, String msg, InetAddress ip, DatagramSocket socUDP){
    try {
      String time = new Timestamp(System.currentTimeMillis()).toString();
      Message m = Message.msg(user, time, msg);
      // Pour l'instant, je pars du principe que le message à moins de 1024 octets
      byte[] mBytes = new byte[1024];
      mBytes = m.messageToStr().getBytes();
      if (mBytes.length < 1024){
        DatagramPacket dp = new DatagramPacket(mBytes, mBytes.length, ip, 7201); // ici c'est changé
        socUDP.send(dp);
        // Ack
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socUDP.receive(receivePacket);
        // afficher le message
        String reponse = new String(receivePacket.getData());
        System.out.println(reponse);
      } else {
        System.out.println("message trop long");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void readMsg(String name){
    BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(name + ".txt"));
			String msg = read.readLine();
			while (msg != null) {
				System.out.println(msg);
				msg = read.readLine();
			}
			read.close();
		} catch (IOException e) {
      System.out.println("Lecture impossible");
			e.printStackTrace();
		}
  }
}
