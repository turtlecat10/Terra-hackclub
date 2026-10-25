package src;
import java.util.*;
import java.util.concurrent.CountDownLatch;
public class Run {
    public static CountDownLatch inputLatch;
    public static CountDownLatch wildLatch;
    public static boolean turnFinished;
    public static boolean wildFinished;
    public static boolean turnPassed = false;
    public static void shuffle(){
        Main.cards.addAll(Main.discard.subList(1, Main.discard.size()));
        Main.discard.subList(1,Main.discard.size()).clear();
        Collections.shuffle(Main.cards);
        
    }
    public static String draw()
    {
        if(Main.cards.size() == 0){
            shuffle();
        }
        String card = Main.cards.get(0);
        Main.cards.remove(0);
        return card;
    }
    public static void turn(Scanner turnScan){
        Main.seven = false;
        Main.turnAgain = false;
        Main.removed = false;
        Main.pass = false;
        if(Main.turnOrder < 0){
            Main.turnOrder = Main.numPlayers-1;
        }else if(Main.turnOrder >= Main.numPlayers){
            Main.turnOrder = 0;
        }
        int prevPlayer = Main.turnOrder - Main.direction;
        if(prevPlayer < 0){
            prevPlayer = Main.numPlayers - 1;
        } else if(prevPlayer >= Main.numPlayers){
            prevPlayer = 0;
        }
        if(Main.declaredUno){
            if(Main.hands.get(prevPlayer).size() != 1){
                Main.hands.get(prevPlayer).add(0, draw());
                Main.hands.get(prevPlayer).add(0, draw());
            }
            Main.declaredUno = false;
        }
        Main.wildTime = false;
        Main.clear();
        Main.selectedCards.clear();
        Main.selected = 0;

        if(Main.discard.get(0).charAt(0) != 'W'){
            Main.colour = Main.discard.get(0).substring(0,1);
        }
        while(Main.colourRoulette){ 
            String drawn = draw();
            Main.hands.get(Main.turnOrder).add(drawn);
            if(drawn.substring(0,1).equals(Main.colour) || drawn.substring(0,1).equals("W")){
                Main.colourRoulette = false;
                Main.skipTurn++;
            }
        }
        if(Main.skipTurn > 0){
            Main.skipTurn--;
        }else{
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
                for(int i = Main.numPlayers -1; i >= 0; i--){
                    if(Main.hands.get(i).size() >= Main.maxHandSize){
                        System.out.println("Player "+Main.playerNames.get(i)+" is eliminated");
                        Main.losers.add(0,Main.playerNames.get(i));
                        while(!Main.hands.get(i).isEmpty()){
                            Main.discard.add(Main.hands.get(i).remove(0));
                        }

                        Main.hands.remove(i);
                        Main.playerNames.remove(i);
                        Main.numPlayers--;
                        if(Main.numRemain == Main.numPlayers){
                            Main.running = false;
                            break;
                        }
                         if (i == Main.turnOrder) {
                            Main.removed = true;
                            turnFinished = true;
                            turnPassed = true;
                        }else if (i < Main.turnOrder) {
                            Main.turnOrder --;
                        }
                        if(Main.turnOrder < 0){
                            Main.turnOrder = Main.numPlayers-1;
                        }else if(Main.turnOrder >= Main.numPlayers){
                            Main.turnOrder = 0;
                        }
                        break;
                    }
                }
                if(Main.prevDeclaredUno){
                    System.out.println("Previous Person Declared Uno");
                }else{
                    System.out.println("Previous person didn't declare Uno");
                }
                if(Main.declaredUno){
                    System.out.println("You have declared Uno");
                }else{
                    System.out.println("You have not declared uno");
                }
                for(int i = 0; i < Main.numPlayers; i++){
                    int player = (Main.turnOrder+i*Main.direction) % Main.numPlayers;
                    if(player < 0){
                        player += Main.numPlayers;
                    }
                    System.out.print("Player: "+Main.playerNames.get(player)+" Hand: "+Main.hands.get(player).size()+", ");
                }
                System.out.println("");
                System.out.println("Top card: "+Main.discard.get(0)+" Colour: "+Main.colour);
                display();
                inputLatch = new CountDownLatch(1);
                try{
                    inputLatch.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            Main.prevOrder = Main.turnOrder;
            Main.prevDeclaredUno = Main.declaredUno;
            play();
        }
        if(!Main.removed && !Main.turnAgain){
            Main.turnOrder += Main.direction;
        }
    }
    public static void releaseBlock(){
        if(inputLatch != null){
            inputLatch.countDown();
        }
    }
    public static void releaseWild(){
        if(wildLatch != null){
            wildLatch.countDown();
        }
    }
    public static void display(){
        System.out.println("Discard pile: "+Main.discard);
        // System.out.println(Main.tempDiscard);
        System.out.println("Adding: "+Main.adding+" Direction: "+Main.direction+" Cards left: "+Main.cards.size());
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
                System.out.print("[pass], ");
            }else{
                System.out.print("pass, ");
            }
            if(Main.selected == Main.hands.get(Main.turnOrder).size()+2){
                System.out.println("[Uno]");
            }else{
                System.out.println("Uno");
            }
            for(int i = 0; i < Main.selectedCards.size(); i++){
                System.out.print(Main.hands.get(Main.turnOrder).get(Main.selectedCards.get(i))+", ");
            }
            System.out.println("");
            System.out.println("Space: Select/Deselect card | Left/Right: Navigate | Enter: Confirm");
    }
    public static void enter(){
        if(Main.seven){
            ArrayList<String> myHand = Main.hands.get(Main.turnOrder);
            ArrayList<String> targetHand = Main.hands.get(Main.target);
            Main.hands.set(Main.turnOrder, targetHand);
            Main.hands.set(Main.target, myHand);
            System.out.println("Swapped hands with " + Main.playerNames.get(Main.target) + "!");

            Main.chosen = true;
            Main.seven = false; 
            releaseBlock();
            return;
        }
        if(Main.hands.get(Main.turnOrder).size()+1 == Main.selected){
            Main.pass = true;
        }else{
            Main.pass = false;
        }
        if(!Main.wildTime){
            turnFinished = checkLegal();
            if(turnFinished){
                releaseBlock();
            }else{
                wait(1500);
                releaseBlock();
            }
        }else{
            wildFinished = true;
            releaseWild();
        }
        
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
        if(Main.seven){
            if(Main.target != 0){
                if(Main.target == Main.turnOrder + 1 && Main.direction == -1){ 
                    Main.target--;
                }
                Main.target--;
                if(Main.target == Main.turnOrder) {
                    Main.target--;
                } 
            } else {
                Main.target = Main.numPlayers - 1; // Wrap around to end
                if(Main.target == Main.turnOrder) {
                    Main.target--;
                }
            }
            if(Main.target < 0) {
                Main.target = 0;
            }
            releaseBlock();
            return;
        }
        if(!Main.wildTime){
            if(Main.selected != 0){
                Main.selected--;
            }
            releaseBlock();
        }else{
            if(Main.wildSelected != 0){
                Main.wildSelected--;
            }
            releaseWild();
        }
    }
    public static void right(){
         if(Main.seven){
            Main.target++;
            if(Main.target >= Main.numPlayers) {
                Main.target = 0;
            }
            if(Main.target == Main.turnOrder) {
                Main.target++;
            }
            if(Main.target >= Main.numPlayers) {
                Main.target = (Main.turnOrder == 0) ? 1 : 0;
            }
            releaseBlock();
            return;
        }
        if(!Main.wildTime){
            if(Main.selected != Main.hands.get(Main.turnOrder).size()+2){
                Main.selected++;
            }
            releaseBlock();
        }else{
            if(Main.wildSelected != 3){
                Main.wildSelected++;
            }
            releaseWild();
        }
    }
    public static void one(){
        turnPassed = false;
        releaseBlock();
    }
    public static boolean checkLegal(){
        int handSize = Main.hands.get(Main.turnOrder).size();
        if(Main.selected == handSize){
            if(Main.adding > 0){
                Main.pass = true;
                System.out.println("You drew "+Main.adding+" cards");
                wait(1500);
                for(int i = 0; i < Main.adding; i++){
                    Main.hands.get(Main.turnOrder).add(draw());
                }
                Main.adding = 0;
                return true;
            }else{
                Main.hands.get(Main.turnOrder).add(draw());
                return false;
            }
        }
        if(Main.selected == handSize+1){
            if(Main.adding > 0){
                System.out.println("You can't pass while having an active draw requirement");
                return false;
            }
            return true;
        }
        if(Main.selected == handSize+2){
            if(!Main.prevDeclaredUno && Main.hands.get(Main.prevOrder).size() == 1){
                Main.hands.get(Main.prevOrder).add(draw());
                Main.hands.get(Main.prevOrder).add(draw());
            }else if(!Main.declaredUno){
                Main.declaredUno = true;
            }else{
                Main.declaredUno = false;
            }
            return false;
        }
        if(Main.selectedCards.isEmpty()){
            System.out.println("Please select cards");
            return false;
        }

        Main.tempDiscard.clear();
        Main.tempDiscard.addAll(Main.discard);
        Main.tempColour = Main.colour;
        boolean discardAll = false;
        boolean colourRoulette = false;
        for(int i = 0; i < Main.selectedCards.size(); i++){
            String discard = Main.tempDiscard.get(0);
            String card = Main.hands.get(Main.turnOrder).get(Main.selectedCards.get(i));
            String discardSymbol = discard.substring(1);
            String cardSymbol = card.substring(1); 
            if(cardSymbol.equals("DA")){
                if(discardAll){
                    System.out.println("You can't play multiple discard alls in the same turn");
                    return false;
                }else{
                    discardAll = true;
                }
            }
            if(cardSymbol.equals("CR")){
                if(colourRoulette){
                    System.out.println("You can't play multiple Colour Roulettes in the same turn");
                    return false;
                }else{
                    colourRoulette = true;
                }
            }
            if(i == 0){
                if(card.charAt(0) == 'W'){

                }else if(card.charAt(0) == discard.charAt(0)){

                }else if(card.substring(0,1).equals(Main.tempColour)){

                }else if(cardSymbol.equals(discardSymbol)){

                }else{
                    System.out.println("Colour or value doesn't match");
                    return false;
                }
            }else{
                if(!cardSymbol.equals(discardSymbol)){
                    System.out.println("You need to have the same symbol on all of them to play");
                    return false;
                }
            }
            String plusPart = "";
            int discardLast = 0;
            int last = 0;
            if(cardSymbol.contains("+")){
                plusPart = "+";
                String numericPart = cardSymbol.replaceAll("[^0-9]","");
                last = Integer.parseInt(numericPart);
            }
            if(discardSymbol.contains("+")){
                 String numericPart = discardSymbol.replaceAll("[^0-9]","");
                 discardLast = Integer.parseInt(numericPart);
            }

            if(Main.adding > 0){
                if(plusPart.equals("+")){
                    if(last >= discardLast){

                    }else{
                        System.out.println("Please give a + card that is bigger or equal to the previous one");
                        return false;
                    }
                }else{
                    System.out.println("Please give a + card that is bigger or equal to the previous one");
                    return false;
                }
            }
            Main.tempDiscard.add(0,card);
        }
        return true;
    }
    public static void wild(){
        wildFinished = false;
        Main.wildTime = true;
        while(!wildFinished){
            Main.clear();
            displayWild();
            wildLatch = new CountDownLatch(1);
            try{
                wildLatch.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        switch (Main.wildSelected){
            case 0:
                Main.colour = "R";
                break;
            case 1:
                Main.colour = "Y";
                break;
            case 2:
                Main.colour = "G";
                break;
            case 3:
                Main.colour = "B";
                break;
        }
        Main.wildTime = false;
    }
    public static void play(){
        if(Main.selected != Main.hands.get(Main.turnOrder).size()+1){
            List<String> cardsRemove = new ArrayList<>();
            for(int i = 0; i < Main.selectedCards.size(); i++){
                int cardIndex = Main.selectedCards.get(i);
                String cardPlayed = Main.hands.get(Main.turnOrder).get(cardIndex);
                cardsRemove.add(cardPlayed);
                Main.discard.add(0,cardPlayed);
            }
            for(String card : cardsRemove){
                Main.hands.get(Main.turnOrder).remove(card);
            }
            for(String cardPlayed : cardsRemove){
                effect(cardPlayed);
            }
            Main.selectedCards.clear();
            if(!Main.pass){
                if(Main.discard.get(0).charAt(0) == 'W'){
                    wild();
                }
            }
        }
        if(Main.hands.get(Main.turnOrder).size() == 0){
            Main.removed = true;
            Main.order.add(Main.playerNames.get(Main.turnOrder));
            Main.hands.remove(Main.turnOrder);
            Main.playerNames.remove(Main.turnOrder);
            Main.numPlayers--;
            if(Main.numRemain == Main.numPlayers){
                Main.running = false;
            }
        }
        Main.selected = 0;
    }
    public static void displayWild(){
        System.out.println(Main.hands.get(Main.turnOrder));
        for(int i = 0; i < 4; i++){
            if(i == 0){
                if(Main.wildSelected == 0){
                    System.out.print("[Red], ");
                }else{
                    System.out.print("Red, ");
                }
            }else if(i == 1){
                if(Main.wildSelected == 1){
                    System.out.print("[Yellow], ");
                }else{
                    System.out.print("Yellow, ");
                }
            }else if(i == 2){
                if(Main.wildSelected == 2){
                    System.out.print("[Green], ");
                }else{
                    System.out.print("Green, ");
                }
            }else if(i == 3){
                if(Main.wildSelected == 3){
                    System.out.print("[Blue]");
                }else{
                    System.out.print("Blue");
                }
            }
        }
    }
    public static void effect(String card){
        String cardEnding = card.substring(1);
        char cardStart = card.charAt(0);

        if(cardEnding.contains("+")){
            Main.adding += Integer.parseInt(cardEnding.replaceAll("[^0-9]",""));
        }
        if(cardEnding.contains("R")){
            if(Main.numPlayers > 2){
                Main.direction = Main.direction*-1;
            }else{
                Main.skipTurn++;
            }
        }
        if(cardEnding.contains("SE")){
            Main.turnAgain = true;
        }else if(cardEnding.contains("S")){
            Main.skipTurn++;
        }else if(cardEnding.contains("DA")){
            List<String> playerHand = Main.hands.get(Main.turnOrder);
            Iterator<String> iterator = playerHand.iterator();
            while(iterator.hasNext()){
                String handCard = iterator.next();
                if(handCard.charAt(0) == cardStart){
                    Main.discard.add(0,handCard);
                    iterator.remove();
                }
            }
        }else if(cardEnding.contains("CR")){
            Main.colourRoulette = true;
        }
        if(Main.file ==  "UnoNoMercy.txt"){
            if(cardEnding.equals("0")){
                System.out.println("Passing hand in direction of play");
                wait(1500);
                if(Main.direction == 1){
                    ArrayList<String> tempLastHand = Main.hands.get(Main.numPlayers - 1);
                    for(int i = Main.numPlayers - 1; i > 0; i--){
                        Main.hands.set(i, Main.hands.get(i-1));
                    }
                    Main.hands.set(0,tempLastHand);
                }else{
                    ArrayList<String> tempFirstHand = Main.hands.get(0);
                    for(int i = 0; i < Main.numPlayers-1; i++){
                        Main.hands.set(i,Main.hands.get(i+1));
                    }
                    Main.hands.set(Main.numPlayers - 1, tempFirstHand);
                }
            }else if(cardEnding.equals("7")){
                Main.seven = true;
                sevenMenu();
            }
        }
    }
    public static void wait(int miliseconds){
        try{
            Thread.sleep(miliseconds);
        }catch(InterruptedException e){

        }
    }
    public static void sevenMenu(){
        Main.target = 0;
        Main.chosen = false;
        if(Main.turnOrder == 0 && Main.numPlayers > 1){
            Main.target = 1;
        }
        while(!Main.chosen){
            Main.clear();
            System.out.println("Chose someone to swap hands with");
            for(int i = 0; i < Main.numPlayers; i++){
                if(i == Main.turnOrder){
                    System.out.print("("+Main.playerNames.get(i)+") Size: "+Main.hands.get(i).size()+", ");
                }else if(i == Main.target){
                    System.out.print("["+Main.playerNames.get(i)+"] Size: "+Main.hands.get(i).size()+", ");
                }else{
                    System.out.print(Main.playerNames.get(i)+" Size: "+Main.hands.get(i).size()+", ");
                }
            }
            System.out.println("\nLeft/Right: Chose Target | Enter: Confirm Swap");
            inputLatch = new CountDownLatch(1);
            try{
                inputLatch.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
