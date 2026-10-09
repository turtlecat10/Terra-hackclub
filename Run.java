import java.util.*;
public class Run {
    public static String draw()
    {
        String card = Main.cards.get(0);
        Main.cards.remove(0);
        return card;
    }
    public static void turn(Scanner turnScan){
        Main.clear();
        for(int i = 0; i < Main.numPlayers; i++){
            System.out.println("Pass to "+Main.playerNames.get(i));
            System.out.println("type 1 and press enter if you have passed");
            String var = turnScan.nextLine();
            Main.clear();
            System.out.println("Top card: "+Main.discard.get(0));
            System.out.println(Main.hands.get(i));
        }
    }
}
