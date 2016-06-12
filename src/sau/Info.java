package sau;

import javax.swing.*;

import java.awt.event.ActionListener;

import static sau.Tile.STATUS_KAYAK;

/**
 * Created by kasia on 12.06.16.
 */
public class Info extends JLabel {
    int kayakStatus;
    private Map map;
    public Info(){
        kayakStatus = STATUS_KAYAK;
    }

    public void setMap(Map map){
        this.map = map;
    }

    public void update(){
        Kayak kayak = map.getKayak();
        Tile tile = kayak.getTiles().get(0);
        String text1 = "ShoreLeft = " + Integer.toString(kayak.distanceToLeftShore()); //3 kayak, 4 collision, 2 obstacle, 1 free
        String text2 = " | ShoreRight = " + Integer.toString(kayak.distanceToLeftShore()); //3 kayak, 4 collision, 2 obstacle, 1 free
        String text3 = "    |AheadR = " + Integer.toString(kayak.distanceToRockAhead());
        String text4 = " LeftR= " + Integer.toString(kayak.distanceToRockLeft());
        String text5 = "    | RightR = " + Integer.toString(kayak.distanceToRockRight());
        setText(text4+ text5 + text3  );
    }
}
