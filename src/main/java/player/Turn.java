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
    int ZugID;

    public Turn(int ZugID, int ZugNummer, String a1, String b1) {
        this.ZugNummer = ZugNummer;
        this.a1 = a1;
        this.b1 = b1;
        this.ZugID = ZugID;
    }
    //Wurzel (ignore)
    public Turn(){}
    public int getID(){
        return ZugID;
    }

    public String toString() {

        return ZugID + "<>" + ZugNummer + ":" + a1 + "-" + b1;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Referenzgleichheit
        if (obj == null || getClass() != obj.getClass()) return false; // Unterschiedliche Klassen

        Turn other = (Turn) obj; // Cast
        return this.ZugNummer == other.ZugNummer &&
                this.a1.equals(other.a1) &&
                this.b1.equals(other.b1) &&
                this.ZugID == other.ZugID; // Vergleich der Strings
    }
}
