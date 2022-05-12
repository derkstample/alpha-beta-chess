import java.util.*;

public class CheckChecker {
    private Board b;
    private ArrayList<Piece> wPieces;
    private ArrayList<Piece> bPieces;
    private ArrayList<Square> movableSquares;
    private ArrayList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,ArrayList<Piece>> wMoves;
    private HashMap<Square,ArrayList<Piece>> bMoves;
    
    public CheckChecker(Board b,ArrayList<Piece> wPieces,ArrayList<Piece> bPieces,King wk,King bk){
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;
        
        // Initialize other fields
        squares = new ArrayList<Square>();
        movableSquares = new ArrayList<Square>();
        wMoves = new HashMap<Square,ArrayList<Piece>>();
        bMoves = new HashMap<Square,ArrayList<Piece>>();
        
        Square[][] brd = b.getSquareArray();
        
        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                wMoves.put(brd[y][x], new ArrayList<Piece>());
                bMoves.put(brd[y][x], new ArrayList<Piece>());
            }
        }
        update();
    }
    public void update() {
        // Iterators through pieces
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();
        
        // empty moves and movable squares at each update
        for(ArrayList<Piece> pieces:wMoves.values()){
            pieces.removeAll(pieces);
        }        
        for(ArrayList<Piece> pieces:bMoves.values()){
            pieces.removeAll(pieces);
        }
        movableSquares.removeAll(movableSquares);
        // Add each move white and black can make to map
        while (wIter.hasNext()) {
            Piece p = wIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }

                ArrayList<Square> mvs = p.getLegalMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    ArrayList<Piece> pieces = wMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }
        
        while (bIter.hasNext()) {
            Piece p = bIter.next();
            
            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    wIter.remove();
                    continue;
                }
                
                ArrayList<Square> mvs = p.getLegalMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    ArrayList<Piece> pieces = bMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }
    }
    public boolean checkCheck(boolean color) {
        update();
        if(color){          //checking for black check
            Square sq = bk.getPosition();
            if (wMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            } else return true;
        }else{              //check for white check
            Square sq = wk.getPosition();
            if (bMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            } else return true;
        }
        
    }
    public boolean BlackInCheck() {
        boolean checkmate = true;
        if (!this.checkCheck(true)) return false;
        // threat can be evaded?
        if (canEvade(wMoves, bk)) checkmate = false;
        // threat can be captured?
        ArrayList<Piece> threats = wMoves.get(bk.getPosition());
        if (canCapture(bMoves, threats, bk)) checkmate = false;
        // threat can be blocked?
        if (canBlock(threats, bMoves, bk)) checkmate = false;
        return checkmate;
    }
    public boolean WhiteInCheck() {
        boolean checkmate = true;
        if (!this.checkCheck(false)) return false;
        // threat can be evaded?
        if (canEvade(bMoves, wk)) checkmate = false;
        // threat can be captured?
        ArrayList<Piece> threats = bMoves.get(wk.getPosition());
        if (canCapture(wMoves, threats, wk)) checkmate = false;
        // threat can be blocked?
        if (canBlock(threats, wMoves, wk)) checkmate = false;
        return checkmate;
    }
    private boolean canEvade(Map<Square,ArrayList<Piece>> tMoves, King k) {
        //returns whether or not the threatened king can evade the threat
        boolean evade=false;
        ArrayList<Square> kingsMoves=k.getLegalMoves(b);
        Iterator<Square> iterator=kingsMoves.iterator();
        // If king is not threatened at some square, it can evade
        while(iterator.hasNext()){
            Square threatSquare = iterator.next();
            if(!tryMove(k,threatSquare))continue;
            if(tMoves.get(threatSquare).isEmpty()){
                movableSquares.add(threatSquare);
                evade=true;
            }
        }
        return evade;
    }

    private boolean canCapture(Map<Square,ArrayList<Piece>> poss,ArrayList<Piece> threats,King k){
        //returns whether or not the threatened king can capture the threat
        boolean capture = false;
        if(threats.size()==1){
            Square threatSquare=threats.get(0).getPosition();
            if(k.getLegalMoves(b).contains(threatSquare)) {
                movableSquares.add(threatSquare);
                if(tryMove(k,threatSquare)){
                    capture=true;
                }
            }
            ArrayList<Piece> caps=poss.get(threatSquare);
            ArrayList<Piece> capturers=new ArrayList<Piece>();
            capturers.addAll(caps);
            if(!capturers.isEmpty()){
                movableSquares.add(threatSquare);
                for(Piece p:capturers){
                    if(tryMove(p,threatSquare)){
                        capture=true;
                    }
                }
            }
        }
        return capture;
    }

    private boolean canBlock(ArrayList<Piece> threats,Map <Square,ArrayList<Piece>> blockMoves, King k){
        //gross spaghetti to determine if checked king can be protected by another piece
        boolean blockable = false;
        if(threats.size()==1){
            Square threatSquare = threats.get(0).getPosition();
            Square kingSquare = k.getPosition();
            Square[][] boardArray = b.getSquareArray();
            if(kingSquare.getXPos() == threatSquare.getXPos()){
                int max = Math.max(kingSquare.getYPos(),threatSquare.getYPos());
                int min = Math.min(kingSquare.getYPos(),threatSquare.getYPos());
                
                for (int i=min+1;i<max;i++){
                    ArrayList<Piece> blocks=blockMoves.get(boardArray[i][kingSquare.getXPos()]);
                    ArrayList<Piece> blockers=new ArrayList<Piece>();
                    blockers.addAll(blocks);
                    if (!blockers.isEmpty()) {
                        movableSquares.add(boardArray[i][kingSquare.getXPos()]);
                        for (Piece p:blockers) {
                            if (tryMove(p,boardArray[i][kingSquare.getXPos()])) {
                                blockable = true;
                            }
                        }
                    }
                }
            }
            if (kingSquare.getYPos()==threatSquare.getYPos()) {
                int max = Math.max(kingSquare.getXPos(),threatSquare.getXPos());
                int min = Math.min(kingSquare.getXPos(),threatSquare.getXPos());
                for (int i=min+1;i<max;i++){
                    ArrayList<Piece> blocks=blockMoves.get(boardArray[kingSquare.getYPos()][i]);
                    ArrayList<Piece> blockers=new ArrayList<Piece>();
                    blockers.addAll(blocks);
                    if (!blockers.isEmpty()) {
                        movableSquares.add(boardArray[kingSquare.getYPos()][i]);
                        for (Piece p:blockers) {
                            if (tryMove(p, boardArray[kingSquare.getYPos()][i])) {
                                blockable = true;
                            }
                        }
                    }
                }
            }
            Class<? extends Piece> threatClass = threats.get(0).getClass();
            if (threatClass.equals(Queen.class)||threatClass.equals(Bishop.class)){
                //diagonal check
                int kX = kingSquare.getXPos();
                int kY = kingSquare.getYPos();
                int tX = threatSquare.getXPos();
                int tY = threatSquare.getYPos();
                
                if(kX>tX&&kY>tY){
                    for (int i=tX+1;i<kX;i++){
                        tY++;
                        ArrayList<Piece> blks=blockMoves.get(boardArray[tY][i]);
                        ArrayList<Piece> blockers=new ArrayList<Piece>();
                        blockers.addAll(blks);
                        if(!blockers.isEmpty()){
                            movableSquares.add(boardArray[tY][i]);
                            for (Piece p:blockers) {
                                if (tryMove(p,boardArray[tY][i])) {
                                    blockable=true;
                                }
                            }
                        }
                    }
                }
                if(kX>tX&&tY>kY){
                    for(int i=tX+1;i<kX;i++){
                        tY--;
                        ArrayList<Piece> blks=blockMoves.get(boardArray[tY][i]);
                        ArrayList<Piece> blockers=new ArrayList<Piece>();
                        blockers.addAll(blks);
                        if(!blockers.isEmpty()){
                            movableSquares.add(boardArray[tY][i]);
                            for(Piece p:blockers){
                                if(tryMove(p,boardArray[tY][i])){
                                    blockable=true;
                                }
                            }
                        }
                    }
                }
                if(tX>kX&&kY>tY){
                    for(int i=tX-1;i>kX;i--){
                        tY++;
                        ArrayList<Piece> blks=blockMoves.get(boardArray[tY][i]);
                        ArrayList<Piece> blockers=new ArrayList<Piece>();
                        blockers.addAll(blks);
                        if (!blockers.isEmpty()) {
                            movableSquares.add(boardArray[tY][i]);
                            for (Piece p : blockers) {
                                if (tryMove(p, boardArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
                if(tX>kX&&tY>kY){
                    for(int i=tX-1;i>kX;i--){
                        tY--;
                        ArrayList<Piece> blks=blockMoves.get(boardArray[tY][i]);
                        ArrayList<Piece> blockers=new ArrayList<Piece>();
                        blockers.addAll(blks);
                        if(!blockers.isEmpty()){
                            movableSquares.add(boardArray[tY][i]);
                            for(Piece p:blockers){
                                if (tryMove(p,boardArray[tY][i])) {
                                    blockable=true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return blockable;
    }
    public ArrayList<Square> getAllowableSquares(boolean b) {
        movableSquares.removeAll(movableSquares);
        if (checkCheck(false)) {
            WhiteInCheck();
        } else if (checkCheck(true)) {
            BlackInCheck();
        }
        return movableSquares;
    }
    public boolean tryMove(Piece p, Square sq) {
        Piece c = sq.getOccPiece();
        boolean canMove = true;
        Square init = p.getPosition();
        p.move(sq);
        update();
        if (p.getColor() == 0 && checkCheck(true)) canMove = false;
        else if (p.getColor() == 1 &&checkCheck(false)) canMove = false; 
        p.move(init);
        if (c != null) sq.put(c);
        update();
        movableSquares.addAll(squares);
        return canMove;
    }
}