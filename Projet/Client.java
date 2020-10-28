import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client{

  public static void main(String[] args)
  {
    Socket soc = null;
    try {
      // 3 façon de se connecter différemment :
      //                          type = Inet.address
      // soc 1
      // soc = new Socket(InetAddress.getByName("localhost:1027"), 1027);
      //                          type = string
      // soc 2
      soc = new Socket("localhost", 1027);
      //Socket soc3 = new Socket("toto", 1027);


      // Construction Socket avec spécification de par ou va la réponse.

      // InetAddress localhost = InetAddress.getByName("192.168.2.44");
      // 0 ==> port de réponse, 0 = n'importe lequel.
      // Socket soc4 = new Socket("localhost:1027", 1027, localhost, 0);
      System.out.println("Port de communication côté serveur : " + soc.getPort());

      BufferedInputStream bis = new BufferedInputStream(soc.getInputStream());
      String content = "";
      int stream;
      while((stream = bis.read()) != -1){
        //content += (char)stream;
        System.out.print((char)stream);
      }

      // Demande de connection en bonne et due forme
      BufferedOutputStream bos = new BufferedOutputStream(soc.getOutputStream());
      String message;
      Scanner inFromUser = new Scanner(System.in);
      System.out.print("\nVeuillez indiquer votre nom d'utilisateur (première connection)\nOu votre #token (pensez au #) :\n");
      String sentence = inFromUser.nextLine();
      message = Message.connect(sentence).messageToStr();
      System.out.println(message);
      bos.write(message.getBytes());
      bos.flush();

      System.out.println("\nQuevoulez-vous faire ? (1 -> envoyer annonce, q -> déconnexion)\n");
      String action = inFromUser.nextLine();
      switch(action) {
        case "1":
          message = Message.postAnc().messageToStr();
          bos.write(message.getBytes());
          bos.flush();
          break;
        case "q":
          break;
        default :
          System.out.println("Action inconnue");
          break ;
      }


      //System.out.println("message du serveur = " + content);
    }
    catch (UnknownHostException e){
      // pour la soc 3 car toto n'existe pas
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
        }
        catch (IOException e) {
          e.printStackTrace();
          soc = null;
        }
      }
    }

  }

}
