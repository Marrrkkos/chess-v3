package player;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class moveTree implements Serializable {

    @Serial
    private static final long serialVersionUID = 3403983621295530595L;
    private final Knoten Wurzel;

    public moveTree(Turn turn){
        this.Wurzel = new Knoten(turn);
    }
    public void add(ArrayList<Turn> Zuege){
        Knoten knoten = Wurzel;
        boolean is = false;
        for(Turn turn : Zuege){

            if(!knoten.getChildren().isEmpty()) {
                for (Knoten knoten1 : knoten.getChildren()) {
                    System.out.println("turn.hashCode(): " + turn.hashCode());
                    System.out.println("knoten1.turn.hashCode(): " + knoten1.turn.hashCode());
                    if (knoten1.turn.equals(turn)) {
                        System.out.println("jetzt abeeer");
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
    public void traverseLevelOrder() {

        List<Knoten> aktuelleEbene = new ArrayList<>();
        aktuelleEbene.add(Wurzel);

        while (!aktuelleEbene.isEmpty()) {
            List<Knoten> naechsteEbene = new ArrayList<>();
            for (Knoten knoten : aktuelleEbene) {
                System.out.print(knoten.turn + " ");
                naechsteEbene.addAll(knoten.getChildren());
            }
            System.out.println(); // Zeilenumbruch für die nächste Ebene
            aktuelleEbene = naechsteEbene;
        }
    }
    public ArrayList<Turn> getCurrentChildren(ArrayList<Turn> Zuege){
        ArrayList<Turn> possibleMoves = new ArrayList<>();
        Knoten knoten = Wurzel;
        for(Turn turn : Zuege){
            if(!knoten.getChildren().isEmpty()) {
                for (Knoten knoten1 : knoten.getChildren()) {

                    if (knoten1.turn.equals(turn)) {
                        knoten = knoten1;
                    }
                }
            }
        }

        for(Knoten knoten1: knoten.getChildren()){
            possibleMoves.add(knoten1.turn);
        }
        return possibleMoves;
    }
    public String toString() {
        List<String> pfade = new ArrayList<>();
        erstellePfade(Wurzel, "", pfade);
        return String.join("\n", pfade);
    }

    private void erstellePfade(Knoten knoten, String aktuellerPfad, List<String> pfade) {
        if (knoten == null) return;

        // Füge den aktuellen Knoten zum Pfad hinzu
        aktuellerPfad += knoten.turn;

        // Wenn der Knoten ein Blatt ist, füge den Pfad zur Liste hinzu
        if (knoten.getChildren().isEmpty()) {
            pfade.add(aktuellerPfad);
            return;
        }

        // Rekursiv die Kinder besuchen
        for (Knoten kind : knoten.getChildren()) {
            erstellePfade(kind, aktuellerPfad + " -> ", pfade);
        }
    }
}
