package sau;

/**
 * Created by kasia on 18.06.16.
 */
public class StateSimulator {
    public static State simulateAction(State currentState, Action action){
        int x = currentState.kayakPositions.get(0)/Map.X_TILES_COUNT;
        int y = currentState.kayakPositions.get(0) - x * Map.X_TILES_COUNT;
        Kayak virtualKayak = new Kayak(x, y, currentState.statusBoard);

        //simulate action
        virtualKayak.moveDown();
        virtualKayak.updateStatusBoard();
        if(action.direction == Action.LEFT)
            virtualKayak.moveLeft();
        else if (action.direction == Action.RIGHT)
            virtualKayak.moveRight();
        virtualKayak.updateStatusBoard();

        //move everything one row up
        int statusBoard[][] = virtualKayak.statusBoard;
        for(int i = 1; i < statusBoard.length; i++)
            for(int j = 0; j < statusBoard[0].length; j++)
                statusBoard[i-1][j] = statusBoard[i][j];

        State newState = new State(statusBoard);
        return newState;
    }
}
