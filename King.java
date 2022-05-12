import java.util.ArrayList;

public class King extends Piece {
    public King(int color,Square start,String pic) {
        super(color,start,pic);
    }

    @Override
    public ArrayList<Square> getLegalMoves(Board b) {
        ArrayList<Square> legalMoves = new ArrayList<Square>();
        Square[][] board = b.getSquareArray();
        
        int x = this.getPosition().getXPos();
        int y = this.getPosition().getYPos();
        
        for (int i=1;i>-2;i--) {
            for (int j=1;j>-2;j--) {
                if(!(i==0&&j==0)) {
                    try {
                        if(!board[y+j][x+i].isOccupied()||board[y+j][x+i].getOccPiece().getColor()!= this.getColor())
                            legalMoves.add(board[y+j][x+i]);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return legalMoves;
    }
}