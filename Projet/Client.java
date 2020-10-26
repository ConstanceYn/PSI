import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


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
      //On affiche la page !
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
