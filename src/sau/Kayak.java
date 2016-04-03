package sau;

public class Kayak extends Tile{

    Kayak(int _x, int _y){
        setX(_x);
        setY(_y);
        setStatus(statusKayak);
    }

    public void moveRight(){
        int nextX = this.getX() + Map.tileSize;
        if(nextX <= ((Map.xTilesCount - 1) * Map.tileSize)) {
            setX(nextX);
        }
    }

    public void moveLeft(){
        int nextX = this.getX() - Map.tileSize;
        if(nextX >= 0) {
            setX(nextX);
        }
    }
}
