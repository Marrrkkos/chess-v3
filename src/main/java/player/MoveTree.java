package player;

import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MoveTree implements Serializable {

    @Serial
    private static final long serialVersionUID = 3403983621295530595L;
    private final Knoten Wurzel;

    public MoveTree(Turn turn){
        this.Wurzel = new Knoten(turn);
    }
    public void add(ArrayList<Turn> Zuege){
        Knoten knoten = Wurzel;
        boolean is = false;
        for(Turn turn : Zuege){

            if(!knoten.getChildren().isEmpty()) {
                for (Knoten knoten1 : knoten.getChildren()) {

                    if (knoten1.turn.equals(turn)) {
                        knoten = knoten1;
                        is = true;
                    }
                }
                if (!is) {
                    knoten.addChild(new Knoten(turn));
                    knoten = knoten.getChildren().getLast();

                }
                is = false;
            }else {
                knoten.addChild(new Knoten(turn));
                knoten = knoten.getChildren().getFirst();
            }
        }
    }
    public boolean checkForMoreThenOneMove(ArrayList<Turn> Zuege, boolean colour){
        Knoten knoten = Wurzel;
        boolean is = false;
        int count;
        if(colour) {
            count = 0;  //weiß am zug
        }else {
            count = 1;
        }
        for(Turn turn : Zuege){

            if(!knoten.getChildren().isEmpty()) {
                for (Knoten knoten1 : knoten.getChildren()) {

                    if (knoten1.turn.equals(turn)) {
                        knoten = knoten1;
                        is = true;
                        count++;
                    }
                }
                if(!is){
                    if(count%2 == 0) {
                        if (!knoten.getChildren().isEmpty()) {
                            return false;
                        }
                        return true;
                    }
                }
                is = false;
            }
        }
        System.out.println("EndTrue");
        return true;
    }
    public Knoten getSubTree(ArrayList<Turn> Zuege){
        Knoten knoten = Wurzel;
        boolean is = false;
        for(Turn turn : Zuege){
            for (Knoten knoten1 : knoten.getChildren()) {

                if (knoten1.turn.equals(turn)) {
                    knoten = knoten1;       //parent
                    is = true;
                }
            }
            if(!is){
                return null;
            }
            is = false;
        }
        return knoten;
    }
    public void addKnoten(Knoten knoten){
        Wurzel.getChildren().add(knoten);
    }
    public void remove(ArrayList<Turn> Zuege){
        Knoten knoten = Wurzel;
        Turn removeTurn = Zuege.removeLast();

        for(Turn turn : Zuege){
            for (Knoten knoten1 : knoten.getChildren()) {

                if (knoten1.turn.equals(turn)) {
                    knoten = knoten1;       //parent
                }
            }
        }
            for (Knoten knoten2 : knoten.getChildren()) {

                if (knoten2.turn.equals(removeTurn)) {
                    knoten.getChildren().remove(knoten2);
                    break;
                }
            }
    }

    /**
     * Gets all Saved Moves
     */
    public ArrayList<Turn> getCurrentChildren(ArrayList<Turn> Zuege){
        ArrayList<Turn> possibleMoves = new ArrayList<>();
        Knoten knoten = Wurzel;
        Boolean hasChildren = false;

        for(Turn turn : Zuege){
            if(!knoten.getChildren().isEmpty()) {
                for (Knoten knoten1 : knoten.getChildren()) {

                    if (knoten1.turn.equals(turn)) {
                        knoten = knoten1;
                        hasChildren = true;
                    }
                }
            }
            if(!hasChildren){
                return new ArrayList<>();
            }
            hasChildren = false;
        }

        for(Knoten knoten1: knoten.getChildren()){
            possibleMoves.add(knoten1.turn);
        }
        return possibleMoves;
    }

    public String toString() {
        List<String> pfade = new ArrayList<>();
        System.out.println(erstellePfade());
        return String.join("\n", pfade);
    }

    /**
     * Creates every Line from root to end. It's a method for the Training-aspect of this program
     * @return all saved Lines in List form, so you can play vs the bot to train this opening
     */
    public List<ArrayList<Knoten>> erstellePfade() {
        return erstellePfadeIterativ(this.Wurzel);
    }

    private List<ArrayList<Knoten>> erstellePfadeIterativ(Knoten wurzel) {
        List<ArrayList<Knoten>> pfade = new ArrayList<>(); // Liste aller Pfade

        if (wurzel == null) {
            return pfade;
        }

        Stack<Pair<Knoten, ArrayList<Knoten>>> stack = new Stack<>();

        ArrayList<Knoten> startPfad = new ArrayList<>();
        startPfad.add(wurzel);
        stack.push(new Pair<>(wurzel, startPfad));

        while (!stack.isEmpty()) {
            Pair<Knoten, ArrayList<Knoten>> currentElement = stack.pop();
            Knoten aktuellerKnoten = currentElement.getKey();
            ArrayList<Knoten> aktuellerPfad = currentElement.getValue();

            if (aktuellerKnoten.getChildren().isEmpty()) {
                pfade.add(aktuellerPfad);
            } else {
                for (Knoten kind : aktuellerKnoten.getChildren()) {
                    ArrayList<Knoten> neuerPfad = new ArrayList<>(aktuellerPfad);
                    neuerPfad.add(kind);
                    stack.push(new Pair<>(kind, neuerPfad));
                }
            }
        }

        return pfade;
    }
    public boolean getWurzelColour(){
        return Wurzel.getTurn().getColour();
    }
    public ArrayList<Knoten> getWurzelKnoten(){
        return Wurzel.getChildren();
    }
}
