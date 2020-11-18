import java.net.*;
import java.io.*;
// la partie du client qui attend les communications
// comme MainServeur.java mais en udp
public class ClientUDP implements Runnable{
  private ArrayList<Connexion> contacts;

  public ClientUDP(){
    contacts = new ArrayList<Connexion>();
  }

  public boolean isContact(String nom){
    for (int i =0; i<contacts.size(); i++ ) {
      if(contact.get(i).isEqual(nom))
        return true;
    }
    return false;

  }

  public void addContact(Connexion c){
    contacts.add(c);
  }

  public void run() {

    int port = 7201;

    DatagramSocket serveurSocket = new DatagramSocket(port);
    byte[] receiveData = new byte[1024];

    while(true){
      // attend des messages et les affiches

      // recevoir un message du client
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      serverSocket.receive(receivePacket);
      String sentence = new String(receivePacket.getData());
      System.out.println(sentence);



      InetAddress addr =  receivePacket.getAddress();
      int port = receivePacket.getPort();

      // faire des fonctions pour casser le message et récupérer les infos du paquet
      // String[4] message ==> le tableau de string
      // message[0] = MSG
      // message[1] = emmetteur
      // message[2] = timestamp
      // message[3] = msg

      String nom = ""; // mesage [1]
      if(!isContact(nom))
      {
        // création d'un contact

      }

      // System.out.println(nom +" : " + message[3]); // on affiche l'emmetteur et le message
      // on construit l'ack avec le timestamp
      // et on l'envoie

      //byte[] sendData = message[2].getBytes();
      //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
      //serveurSocket.send(sendPacket);


    }

  }

}
