package player;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Knoten implements Serializable {
    @Serial
    private static final long serialVersionUID = -4660036972729716866L;

    Turn turn;
    ArrayList<Knoten> children;

    public Knoten(Turn turn){
        this.turn = turn;
        this.children = new ArrayList<>();
    }

    public void addChild(Knoten child){
        children.add(child);
    }
    public ArrayList<Knoten> getChildren(){
        return children;
    }
}
