

public class test{

    public static void main(String[] args) {

        Annonce anc = new Annonce();
  
        String test = anc.getDomaine();
        String str = anc.Annonce_to_Client();
        System.out.println();
        System.out.println(str);

        switch(test){
          case "meuble":
            System.out.println("c'est un meuble");
            break;
          default :
            System.out.println("fuck u");
            break;
        }
        //


        // int b = 13;
        // b = test(b);
        // System.out.println(b);

    }

    static public int test(int a)
    {

      return test2(a);
    }

    static public int test2(int a)
    {
      return (a+5);
    }
}
