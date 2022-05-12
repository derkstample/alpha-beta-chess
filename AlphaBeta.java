import java.util.*;

public class AlphaBeta {
    public static boolean minimaxRoot(int depth,Board b,boolean max){
        Map<Piece,ArrayList<Square>> newGameMoves=new HashMap<Piece,ArrayList<Square>>();
        for(Piece bPiece:b.Bpieces){
            newGameMoves.put(bPiece,bPiece.getLegalMoves(b));
        }
        int bestMoveVal=-9999;
        Piece bestPiece=null;
        Square bestMove=null;
        for(Piece p:b.Bpieces){
            for(int i=0;i<newGameMoves.get(p).size();i++){      //try each move sequentially, using alpha beta pruning, to find the most valuable move
                Square newMove=newGameMoves.get(p).get(i);
                Square tempSquare=p.getPosition();
                Piece tempPiece=newMove.getOccPiece();
                p.move(newMove);
                int val=minimax(depth-1,b,-10000,10000,!max);   //enters a recursive subroutine
                p.move(tempSquare);       
                if(tempPiece!=null)newMove.put(tempPiece);             //undo the last move
                if(val>=bestMoveVal){
                    bestMoveVal=val;
                    bestMove=newMove;
                    bestPiece=p;
                }
            }
        }
        return bestPiece.move(bestMove); //we found and executed the best move!
    }
    public static int minimax(int depth,Board b,int alpha,int beta,boolean max){  //minimax algorithm for all possible moves for a given piece.
        if(depth==0)return -evalBoard(b);
        Map<Piece,ArrayList<Square>> newGameMoves=new HashMap<Piece,ArrayList<Square>>();
        for(Piece bPiece:b.Bpieces){
            newGameMoves.put(bPiece,bPiece.getLegalMoves(b));
        }
        if(max){
            int bestMove=-9999;
            for(Piece p:b.Bpieces){
                for(int i=0;i<newGameMoves.get(p).size();i++){
                    Square newMove=newGameMoves.get(p).get(i);
                    Square tempSquare=p.getPosition();
                    Piece tempPiece=newMove.getOccPiece();
                    p.move(newMove);
                    bestMove=Math.max(bestMove,minimax(depth-1,b,alpha,beta,!max));
                    p.move(tempSquare);
                    if(tempPiece!=null)newMove.put(tempPiece);
                    alpha=Math.max(alpha,bestMove);
                    if(beta<=alpha)return bestMove;
                }
            }
            return bestMove;
        }else{
            int bestMove=9999;
            for(Piece p:b.Bpieces){
                for(int i=0;i<newGameMoves.get(p).size();i++){
                    Square newMove=newGameMoves.get(p).get(i);
                    Square tempSquare=p.getPosition();
                    Piece tempPiece=newMove.getOccPiece();
                    p.move(newMove);
                    bestMove=Math.min(bestMove,minimax(depth-1,b,alpha,beta,!max));
                    p.move(tempSquare);
                    if(tempPiece!=null)newMove.put(tempPiece);
                    beta=Math.min(beta,bestMove);
                    if(beta<=alpha)return bestMove;
                }
            }
            return bestMove;
        }
    };
      
    public static int evalBoard(Board b){
        int val=0;
        for(Piece bp:b.Bpieces)val+=getPieceValue(bp);
        for(Piece wp:b.Wpieces)val+=getPieceValue(wp);
        return val;
    }
    public static int getPieceValue(Piece p){
        if(p==null)return 0;
        String piece=p.toString();
        int val=0;
        if(piece.contains("Pawn"))val=10;
        else if(piece.contains("Rook"))val=50;
        else if(piece.contains("Knight"))val=30;
        else if(piece.contains("Bishop"))val=30;
        else if(piece.contains("Queen"))val=90;
        else if(piece.contains("King"))val=900;
        if(p.getColor()==1)return val;
        else return -val;
    }
}
