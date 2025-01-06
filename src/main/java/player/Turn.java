package player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Turn implements Serializable {
    @Serial
    private static final long serialVersionUID = -3823182165392856506L;
    public int ZugNummer;
    public String a1;
    public String b1;
    public String notationHandled;
    int ZugID;
    boolean playerColour;
    public Turn(int ZugID, int ZugNummer, String a1, String b1, String notationHandled) {
        this.ZugNummer = ZugNummer;
        this.a1 = a1;
        this.b1 = b1;
        this.ZugID = ZugID;
        this.notationHandled = notationHandled;
    }
    //Wurzel
    public Turn(boolean playerColour){
        this.playerColour = playerColour;
    }
    public int getID(){
        return ZugID;
    }

    public String toString() {
        if (a1 != null && b1 != null) {
            return a1 + "-" + b1;
        } else {
            return (playerColour ? "Weiß" : "Schwarz");
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Referenzgleichheit
        if (obj == null || getClass() != obj.getClass()) return false; // Unterschiedliche Klassen

            Turn other = (Turn) obj;

            // Fall 1: Wurzelknoten mit playerColour
            if (this.a1 == null && this.b1 == null) {
                return this.playerColour == other.playerColour;
            }else {
                return this.ZugNummer == other.ZugNummer &&
                        this.a1.equals(other.a1) &&
                        this.b1.equals(other.b1) &&
                        this.ZugID == other.ZugID; // Vergleich der Strings
            }
    }
    public boolean getColour(){
        return this.playerColour;
    }
}
