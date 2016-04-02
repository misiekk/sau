package sau;

public class Tile {
    static private int statusFree = 1;      // wolna
    static private int statusOccupied = 2;  // zajeta
    static private int statusKayak = 3;     // obecnie znajduje sie w niej kajak
    private int x;
    private int y;
    private int status; // free, occupied, kayak in


    Tile(){
    }

    Tile(int _x, int _y){
        this.x = _x;
        this.y = _y;
        this.status = statusFree;
    }

    public int getStatus(){
        return this.status;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
}
