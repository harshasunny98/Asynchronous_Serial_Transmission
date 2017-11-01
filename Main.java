import java.util.Scanner;
public class Main {
   public static void main(String[] args) {
      Transmitter c = new Transmitter();
      Scanner s = new Scanner(System.in);
      int k=0;
      char ch='y';
      System.out.println("Enter the message?");
      do{
      Client pk = new Client(c, k);
      Server ck = new Server(c, k);
      pk.start(); 
      ck.start();{
      System.out.println("Do you wish to transfer another message?\ny:yes n:no");
      k++;}
      ch=s.nextLine().charAt(0);
      }while(ch=='y');
   }
}
class Transmitter {
   private char contents;
   private boolean available = false;
   
   public synchronized char get() {
      while (available == false) {
         try {
            wait();
         } catch (InterruptedException e) {}
      }
      available = false;
      notifyAll();
      return contents;
   }
   public synchronized void put(char value) {
      while (available == true) {
         try {
            wait();
         } catch (InterruptedException e) { } 
      }
      contents = value;
      available = true;
      notifyAll();
   }
}
class Server extends Thread {
   private Transmitter t;
   private int number;
   
   public Server(Transmitter c, int number) {
      t = c;
      this.number = number;
   }
   public void run() {
      char value = 0;
      for (int i = 0; i <= 20; i++) {
         value = t.get();
         System.out.println("Server #" + this.number + " got: " + value);
      }
   }
}
class Client extends Thread {
   private Transmitter t;
   Scanner scanner = new Scanner(System.in);
   private int number;
   public Client(Transmitter c, int number) {
      t = c;
      this.number = number;
   } 
   public void run() {
      //System.out.println("Enter stream of numbers");
      String data = scanner.nextLine();
      char[] a = data.toCharArray();
      int i=0;
           int l = data.length();
      for (i = 0; i < l; i++) {
          char temp = a[i];
         t.put(temp);
         System.out.println("Client #" + this.number + " put: " + temp);
         try {
            sleep((int)(Math.random() * 100));
         } catch (InterruptedException e) { }
      } 
   }
}
