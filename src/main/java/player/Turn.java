package player;

import java.io.Serializable;

public class Turn implements Serializable {

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

        return ZugNummer + ":" + a1 + "-" + b1;
    }
}
