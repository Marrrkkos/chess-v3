package player;

import java.util.ArrayList;

public class Line {
    public ArrayList<Turn> line;
    public Line(ArrayList<Turn> line){
        this.line = line;
    }

    public void add(Turn turn){
        this.line.add(turn);
    }

}
