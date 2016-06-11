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
        String text = "Status of (5,7) = " + Integer.toString(map.getTileArray()[5][7].getStatus()); //3 kayak, 4 collision, 2 obstacle, 1 free
        setText(text);
    }
}
