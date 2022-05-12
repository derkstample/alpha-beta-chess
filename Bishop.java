import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int color, Square start,String pic) {
        super(color,start,pic);
    }
    
    @Override
    public ArrayList<Square> getLegalMoves(Board b) {
        Square[][] board = b.getSquareArray();
        int x = this.getPosition().getXPos();
        int y = this.getPosition().getYPos();
        
        return getDiagNeigbors(board, x, y);
    }
}