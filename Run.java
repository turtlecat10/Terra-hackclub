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
        Main.selectedCards.clear();
        KeyListenerUno listenerWindow = new KeyListenerUno();
        if(Main.turnOrder < 0){
            Main.turnOrder = Main.numPlayers-1;
        }else if(Main.turnOrder >= Main.numPlayers){
            Main.turnOrder = 0;
        }
        Main.clear();
        Main.selected = 0;

        System.out.println("Pass to "+Main.playerNames.get(Main.turnOrder));
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
            System.out.println("Player: "+Main.playerNames.get(Main.turnOrder));
            System.out.println("Top card: "+Main.discard.get(0));
            display();
            inputLatch = new CountDownLatch(1);
            try{
                inputLatch.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        play();
        Main.turnOrder += Main.direction;
    }
    public static void releaseBlock(){
        if(inputLatch != null){
            inputLatch.countDown();
        }
    }
    public static void display(){
        for(int l = 0; l < Main.hands.get(Main.turnOrder).size(); l++){
                if(Main.selected == l){
                    if(Main.selectedCards.contains(l)){
                        System.out.print("[<"+Main.hands.get(Main.turnOrder).get(l)+">], ");
                    }else{
                        System.out.print("["+Main.hands.get(Main.turnOrder).get(l)+"], ");
                    }
                }else{
                    if(Main.selectedCards.contains(l)){
                        System.out.print("<"+Main.hands.get(Main.turnOrder).get(l)+">, ");
                    }else{
                        System.out.print(Main.hands.get(Main.turnOrder).get(l)+", ");
                    }
                }
            }
            if(Main.selected == Main.hands.get(Main.turnOrder).size()){
                System.out.print("[draw], ");
            }else{
                System.out.print("draw, ");
            }
            if(Main.selected == Main.hands.get(Main.turnOrder).size()+1){
                System.out.println("[pass]");
            }else{
                System.out.println("pass");
            }
            for(int i = 0; i < Main.selectedCards.size(); i++){
                System.out.print(Main.hands.get(Main.turnOrder).get(Main.selectedCards.get(i))+", ");
            }
            System.out.println("");
            System.out.println("Press Space to select a card");
    }
    public static void enter(){
        turnFinished = checkLegal();
        releaseBlock();
    }
    public static void space(){
        if(Main.selected < Main.hands.get(Main.turnOrder).size()){
            if(Main.selectedCards.contains(Main.selected)){
                Main.selectedCards.remove(Integer.valueOf(Main.selected));
            }else{
                Main.selectedCards.add(Main.selected);
            }
        }
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
    public static boolean checkLegal(){
        int handSize = Main.hands.get(Main.turnOrder).size();
        if(Main.selected == handSize){
            Main.hands.get(Main.turnOrder).add(draw());
            return false;
        }
        if(Main.selected == handSize+1){
            return true;
        }
        if(Main.selectedCards.isEmpty()){
            System.out.println("Please select cards");
            return false;
        }
        Main.tempDiscard.clear();
        Main.tempDiscard.addAll(Main.discard);
        Main.tempColour = Main.colour;
        for(int i = 0; i < Main.selectedCards.size(); i++){
            String discard = Main.tempDiscard.get(0);
            String card = Main.hands.get(Main.turnOrder).get(Main.selectedCards.get(i));
            if(i == 0){
                if(card.charAt(0) != discard.charAt(0) && !(card.substring(1,card.length()).equals(discard.substring(discard.length())))){
                    if(card.charAt(0) != 'W'){
                        if(!card.substring(0,1).equals(Main.tempColour)){
                            return false;
                        }
                    }
                }
            }else{
                if(!card.substring(1,card.length()).equals(discard.substring(1,discard.length()))){
                    return false;
                }
            }
            if(Main.adding > 0){
                if(!card.substring(1,card.length()).equals(discard.substring(1,discard.length()))){
                    if(!card.substring(1,card.length()).equals("+4")){
                        return false;
                    }
                }
            }
            Main.tempDiscard.add(0,card);
        }
        return true;
    }
    public static void wild(){

    }
    public static void play(){
        for(int i = 0; i < Main.selectedCards.size(); i++){
            String cardPlayed = Main.hands.get(Main.turnOrder).get(Main.selectedCards.get(i));
            Main.discard.add(0, cardPlayed);
            Main.hands.get(Main.turnOrder).remove(cardPlayed);
        }
        if(Main.discard.get(0).charAt(0) == 'W'){
            wild();
        }
    }
}
