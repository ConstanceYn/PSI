import java.net.*;
import java.io.*;
import java.util.*;
// la partie du client qui attend les communications
// comme MainServeur.java mais en udp
public class ClientUDP {
  private ArrayList<Connexion> contacts;

  public ClientUDP(){
    contacts = new ArrayList<Connexion>();
  }

  public boolean isContact(String nom){
    for (int i =0; i<contacts.size(); i++ ) {
      if(contacts.get(i).equals(nom))
      return true;
    }
    return false;

  }

  public void addContact(Connexion c){
    contacts.add(c);
  }

  public static void main(String[] args) {
    ClientUDP cupd = new ClientUDP();

    int port = 7201;

    try{
      DatagramSocket serveurSocket = new DatagramSocket(port);
      byte[] receiveData = new byte[1024];

      while(true){
        // attend des messages et les affiches

        // recevoir un message du client
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serveurSocket.receive(receivePacket);
        String sentence = new String(receivePacket.getData());
        System.out.println(sentence);



        InetAddress addr = receivePacket.getAddress();
        int portCo = receivePacket.getPort();

        // faire des fonctions pour casser le message et récupérer les infos du paquet
        // String[4] message ==> le tableau de string
        // message[0] = MSG
        // message[1] = emmetteur
        // message[2] = timestamp
        // message[3] = msg

        // Si j'ai bien compris, sentence est le message reçu ?
        Message message = Message.strToMessage(sentence);

        if (message.getType().equals("MSG")){
          String nom = message.getArgs()[0];
          if(!cupd.isContact(nom))
          {
            Connexion newCo = new Connexion(nom, addr);
            cupd.contacts.add(newCo);
          }

          System.out.println(nom +" : " + message.getArgs()[2]); // on affiche l'emmetteur et le message
          // on construit l'ack avec le timestamp
          String ack = Message.msgAck().messageToStr();
          System.out.println(ack);
          // et on l'envoie
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    //byte[] sendData = message[2].getBytes();
    //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
    //serveurSocket.send(sendPacket);


  }

}
