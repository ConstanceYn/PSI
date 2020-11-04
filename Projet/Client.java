import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.net.*;
import java.io.*;


public class Client{

  public static void main(String[] args)
  {
    Socket soc = null;
    try {

      soc = new Socket("localhost", 1027);
      System.out.println("Port de communication côté serveur : " + soc.getPort());


      PrintWriter writer = null; // pour écrire au serveur
      BufferedReader networkIn = new BufferedReader( new InputStreamReader(soc.getInputStream())); // pour lire le serveur
      // pour lire l'entrée user
      BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));


      // message de bienvenue du serveur
      String msg = networkIn.readLine();
      System.out.println(msg);


      // liste des requetes possibles :
      String content = "";
      String requetes = "";
      while( !content.equals(".") ){
        content = networkIn.readLine();
        requetes += content + "\n";
      }

      writer = new PrintWriter(soc.getOutputStream());

      // on envoie des requetes au serveur
      boolean continuer = true;
      System.out.println(requetes);
      while(continuer)
      {
        //System.out.println(requetes);
        int action = userIn.read()-'0';
        String message = null;

        switch(action) {
          case 1: // Connection
            System.out.println("Entrez le nom d'utilisateur ou le token : ");
            message = userIn.readLine();
            message = Message.connect(userIn.readLine()).messageToStr();
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
          case 6: // Afficher les annonces d'un domaine (version provisoire, toutes les annonces)
            System.out.println("Quel domaine voulez vous ? : ");
            message = userIn.readLine();
            message = Message.requestAnc(userIn.readLine()).messageToStr();
            //message = "REQUEST_ANC\n.\n";

            break;
          case 7: // Afficher ses annones
            message = Message.requestOwnAnc().messageToStr();
            break;
          case 8:
            continuer = false;
            message = Message.disconnect().messageToStr();
            break;

          default :
            action = -1;
            //System.out.println("Action inconnue");
            break ;
        }
        if (action != -1)
        {
          writer.println(message);
          writer.flush();

          if (action != 8)
          {
            System.out.println("sorti du switch : on attend la réponse");
            content = "";
            String reponse = "";
            while( !content.equals(".") ){
              content = networkIn.readLine();
              reponse += content + "\n";
            }
            System.out.println("reponse du serveur : ");
            System.out.println(reponse);

            System.out.println();

            System.out.println(requetes);
          }
        }


        //continuer = false;


      }
    }
    catch (UnknownHostException e){
      // pour la soc 3 car toto n'existe pas
      e.printStackTrace();
    }
    catch (IOException e){
      e.printStackTrace();
    }
    // finally
    // {
    //   if(soc != null){
    //     try {
    //       soc.close();
    //     }
    //     catch (IOException e) {
    //       e.printStackTrace();
    //       soc = null;
    //     }
    //   }
    // }

  }

}
