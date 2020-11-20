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
import java.lang.Runtime;


public class test{

    public static void main(String[] args) {
      // test ouverture d'un terminal

      try {
        FileWriter fis = new FileWriter("a.txt", true);

        String str = "ceci est un test \n";
        fis.write(str);

        fis.close();

        FileWriter fis2 = new FileWriter("a.txt", true);
        str = "ahhhh";
        fis2.write(str);
        fis2.close();

      }catch(Exception e)
      {}

    }

}
