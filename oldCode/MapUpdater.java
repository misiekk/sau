package sau;

/**
 * Interface for updating global map (statuses of tiles)
 */
public interface MapUpdater {
    void updatePosition();
    void updateCurrentPosition(Tile[][] tilesArray);
    void updatePreviousPosition(Tile[][] tilesArray);
    void calculateXY();
}
