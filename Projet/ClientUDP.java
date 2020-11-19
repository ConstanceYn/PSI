import java.net.*;
import java.io.*;
import java.util.*;
// la partie du client qui attend les communications
// comme MainServeur.java mais en udp
public class ClientUDP {
  private ArrayList<Connexion> contacts;
  private ArrayList<File> fichiers;
  private ArrayList<Message> messages;

  public ClientUDP(){
    contacts = new ArrayList<Connexion>();
    fichiers = new ArrayList<File>();
    messages = new ArrayList<Message>(); // liste des messages dont on attend un ack
          // de la forme : MSG \n emetteur \n timestamp \n msg \n.
  }

  // return la position dans l'array
  public int isContact(String nom){
    for (int i =0; i<contacts.size(); i++ ) {
      if(contacts.get(i).equals(nom))
      return i;
    }
    return (-1);

  }

  public int addContact(Connexion c){
    contacts.add(c);
    // quand on ajoute un contact, on ajoute aussi un fichier pour mettre la discussion
    String str = c.getNom();
    fichiers.add(new File(str + ".txt") );
    return (contacts.size()-1);
  }


  public boolean waitAck(String timestamp){
    for (int i=0;i<messages.size() ;i++ ) {
      if(messages.get(i).getArgs()[1].equals(timestamp))
        return true;
    }
    return false;
  }

  public Message RemoveMessage(String timestamp){
    for (int i=0;i<messages.size() ;i++ ) {
      if(messages.get(i).getArgs()[1].equals(timestamp)){
        return( messages.remove(i) );
      }
    }
    return null;
  }
  public void addMessage(Message m){
    messages.add(m);
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

        // Adresse et port du contact
        InetAddress addr = receivePacket.getAddress();
        int portCo = receivePacket.getPort();

        Message message = Message.strToMessage(sentence);

        if (message.getType().equals("MSG")){
          // message[0] = emmetteur
          // message[1] = timestamp
          // message[2] = msg
          String nom = message.getArgs()[0];
          int position = cupd.isContact(nom);
          if( position == -1)
          {
            Connexion newCo = new Connexion(nom, addr, portCo);
            position = cupd.addContact(newCo);
          }

          try{
            FileOutputStream fis = new FileOutputStream(cupd.fichiers.get(position));
            String str = nom + " : " +message.getArgs()[2] + "\n";
            fis.write(str.getBytes());

          }catch(Exception e){}

          // on construit l'ack avec le timestamp
          String ack = Message.msgAck(message.getArgs()[0], message.getArgs()[1]).messageToStr();
          byte[] sendData = ack.getBytes();
          //System.out.println(ack);
          // et on l'envoie
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, portCo);
        }
        else if(message.getType().equals("MSG_ACK")){
          Message m = cupd.RemoveMessage(message.getArgs()[1]);
          String nom = m.getArgs()[0];
          int position = cupd.isContact(nom);
          if( position == -1)
          {
            // on peut recevoir l'ack de qqn à qui on a envoyé un message sans l'ajouter aux contacts ?
            // je sais pas si c'est possible donc dans le doute je le mets
            Connexion newCo = new Connexion(nom, addr, portCo);
            position = cupd.addContact(newCo);
          }
          try{
            FileOutputStream fis = new FileOutputStream(cupd.fichiers.get(position));
            String str = "moi : " +message.getArgs()[2] + "\n";
            fis.write(str.getBytes());

          }catch(Exception e){}

        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }



  }

}
