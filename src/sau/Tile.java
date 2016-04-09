package sau;

public class Tile {
    static public int STATUS_FREE = 1;
    static public int STATUS_OBSTACLE = 2;
    static public int STATUS_KAYAK = 3;     // kayak is currently in this tile
    private int x, y;           // tile coordinates for painting
    private int indX, indY, oldIndX, oldIndY;     // tile indexes in map array
    private int status;         // free, occupied, kayak in

    Tile(){
    }

    Tile(int _x, int _y, int _idxX, int _idxY){
        this.x = _x;
        this.y = _y;
        this.indX = _idxX;
        this.indY = _idxY;
        this.oldIndX = _idxX;
        this.oldIndY = _idxY;
        this.status = STATUS_FREE;
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
    public int getIndX(){
        return this.indX;
    }
    public int getIndY(){ return this.indY; }
    public int getOldIndX(){
        return this.oldIndX;
    }
    public int getOldIndY(){ return this.oldIndY; }


    public void setX(int _x) {this.x = _x; }
    public void setY(int _y) {this.y = _y; }
    public void setIndX(int _indX) {this.indX = _indX; }
    public void setIndY(int _indY) {this.indY = _indY; }
    public void setOldIndX(int _indX) {this.oldIndX = _indX; }
    public void setOldIndY(int _indY) {this.oldIndY = _indY; }
    public void setStatus(int _status) {this.status = _status; }
}
