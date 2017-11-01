import java.util.Scanner;
public class Main {
   public static void main(String[] args) {
      Transmitter c = new Transmitter();
      Client p1 = new Client(c, 1);
      Server c1 = new Server(c, 1);
      p1.start(); 
      c1.start();
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
      System.out.println("Enter stream of numbers");
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
