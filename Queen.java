
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public ArrayList<Square> getLegalMoves(Board b) {
        ArrayList<Square> legalMoves = new ArrayList<Square>();
        Square[][] board = b.getSquareArray();
        int x = this.getPosition().getXPos();
        int y = this.getPosition().getYPos();
        int[] occups = getSideNeighbors(board, x, y);

        for (int i = occups[0]; i <= occups[1]; i++) {
            if (i != y) legalMoves.add(board[i][x]);
        }
        for (int i = occups[2]; i <= occups[3]; i++) {
            if (i != x) legalMoves.add(board[y][i]);
        }
        ArrayList<Square> bMoves = getDiagNeigbors(board, x, y);
        legalMoves.addAll(bMoves);
        return legalMoves;
    }
    
}