import java.util.*;
import java.util.concurrent.CountDownLatch;
public class Run {
    public static CountDownLatch inputLatch;
    public static boolean turnFinished;
    public static boolean turnPassed = false;
    public static String draw()
    {
        String card = Main.cards.get(0);
        Main.cards.remove(0);
        return card;
    }
    public static void turn(Scanner turnScan){
        Main.clear();
        KeyListenerUno listenerWindow = new KeyListenerUno();
        for(int i = 0; i < Main.numPlayers; i += Main.direction){
            Main.clear();
            Main.selected = 0;
            Main.turnOrder = i;
            System.out.println("Pass to "+Main.playerNames.get(i));
            System.out.println("type 1 if you have passed");
            turnPassed = true; 
            while (turnPassed) {
                inputLatch = new CountDownLatch(1);
                try {
                    inputLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            turnFinished = false;
            while(!turnFinished){
                Main.clear();
                System.out.println("Player: "+Main.playerNames.get(i));
                System.out.println("Top card: "+Main.discard.get(0));
                display();
                inputLatch = new CountDownLatch(1);
                try{
                    inputLatch.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void releaseBlock(){
        if(inputLatch != null){
            inputLatch.countDown();
        }
    }
    public static void display(){
        for(int l = 0; l < Main.hands.get(Main.turnOrder).size(); l++){
                if(Main.selected == l){
                    System.out.print("["+Main.hands.get(Main.turnOrder).get(l)+"], ");
                }else{
                    System.out.print(Main.hands.get(Main.turnOrder).get(l)+", ");
                }
            }
            if(Main.selected == Main.hands.get(Main.turnOrder).size()){
                System.out.print("[draw], ");
            }else{
                System.out.print("draw, ");
            }
            if(Main.selected == Main.hands.get(Main.turnOrder).size()+1){
                System.out.print("[pass]");
            }else{
                System.out.print("pass");
            }
            System.out.println("");
    }
    public static void enter(){
        turnFinished = true;
        releaseBlock();
    }
    public static void left(){
        if(Main.selected != 0){
            Main.selected--;
        }
        releaseBlock();
    }
    public static void right(){
        if(Main.selected != Main.hands.get(Main.turnOrder).size()+1){
            Main.selected++;
        }
        releaseBlock();
    }
    public static void one(){
        turnPassed = false;
        releaseBlock();
    }
}
