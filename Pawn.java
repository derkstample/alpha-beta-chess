import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean wasMoved;
    
    public Pawn(int color, Square initSq, String img_file) {
        super(color, initSq, img_file);
    }
    
    @Override
    public boolean move(Square fin) {
        boolean b = super.move(fin);
        wasMoved = true;
        return b;
    }
    @Override
    public ArrayList<Square> getLegalMoves(Board b) {
        ArrayList<Square> legalMoves = new ArrayList<Square>();
        Square[][] board = b.getSquareArray();
        int x=this.getPosition().getXPos();
        int y=this.getPosition().getYPos();
        int c=this.getColor();
        
        if (c==0) { //white
            if(!wasMoved){    //you can move two squares forward if haven't been moved yet
                if (!board[y+2][x].isOccupied()) {
                    legalMoves.add(board[y+2][x]);
                }
            }
            if (y+1 < 8) {  //move 1 forward if not occupied
                if (!board[y+1][x].isOccupied()) {
                    legalMoves.add(board[y+1][x]);
                }
            }
            if (x+1<8&&y+1<8) { //capturing rules
                if (board[y+1][x+1].isOccupied()&&board[y+1][x+1].getOccPiece().getColor()!=0) {
                    legalMoves.add(board[y+1][x+1]);
                }
            }
            if (x-1>=0&&y+1<8) {
                if (board[y+1][x-1].isOccupied()&&board[y+1][x-1].getOccPiece().getColor()!=0) {
                    legalMoves.add(board[y+1][x-1]);
                }
            }
        }
        
        if (c==1){ //black
            if(!wasMoved){
                if (!board[y-2][x].isOccupied()) {
                    legalMoves.add(board[y-2][x]);
                }
            }
            if (y-1>=0) {
                if (!board[y-1][x].isOccupied()) {
                    legalMoves.add(board[y-1][x]);
                }
            }
            if (x+1<8&&y-1>=0) {
                if (board[y-1][x+1].isOccupied()&&board[y-1][x+1].getOccPiece().getColor()!=1) {
                    legalMoves.add(board[y-1][x+1]);
                }
            }
            if (x-1>=0&&y-1>=0) {
                if (board[y-1][x-1].isOccupied()&&board[y-1][x-1].getOccPiece().getColor()!=1) {
                    legalMoves.add(board[y-1][x-1]);
                }
            }
        }
        return legalMoves;
    }
}