import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class Board extends JPanel implements MouseListener, MouseMotionListener {
	// Resource location constants for piece images
    private static final String WBIMG = "/art/wbishop.png";
	private static final String BBIMG = "/art/bbishop.png";
	private static final String WNIMG = "/art/wknight.png";
	private static final String BNIMG = "/art/bknight.png";
	private static final String WRIMG = "/art/wrook.png";
	private static final String BRIMG = "/art/brook.png";
	private static final String WKIMG = "/art/wking.png";
	private static final String BKIMG = "/art/bking.png";
	private static final String BQIMG = "/art/bqueen.png";
	private static final String WQIMG = "/art/wqueen.png";
	private static final String WPIMG = "/art/wpawn.png";
	private static final String BPIMG = "/art/bpawn.png";
	
	private final Square[][] board;     //data side
    private final Chess c;              //gui side
    public final ArrayList<Piece> Bpieces;
    public final ArrayList<Piece> Wpieces;
    public ArrayList<Square> movable;
    private boolean whiteTurn;
    private Piece currPiece;
    private int currX;
    private int currY;
    private CheckChecker checkChecker;
    private final int SEARCH_DEPTH=3; //how many moves to minimax search
    
    public Board(Chess c) {
        this.c = c;
        board = new Square[8][8];
        Bpieces = new ArrayList<Piece>();
        Wpieces = new ArrayList<Piece>();
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) { //set up board with alternating colors
                    board[x][y] = new Square(this, 1, y, x);
                    this.add(board[x][y]);
                } else {
                    board[x][y] = new Square(this, 0, y, x);
                    this.add(board[x][y]);
                }
            }
        }
        initBoard(); //setup pieces
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
        whiteTurn = true; //player gets to start
    }

    private void initBoard() {
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(0, board[1][x], BPIMG));
            board[6][x].put(new Pawn(1, board[6][x], WPIMG));
        }
        board[7][3].put(new Queen(1, board[7][3], WQIMG));
        board[0][3].put(new Queen(0, board[0][3], BQIMG));
        King bk = new King(0, board[0][4], BKIMG);
        King wk = new King(1, board[7][4], WKIMG);
        board[0][4].put(bk);
        board[7][4].put(wk);
        board[0][0].put(new Rook(0, board[0][0], BRIMG));
        board[0][7].put(new Rook(0, board[0][7], BRIMG));
        board[7][0].put(new Rook(1, board[7][0], WRIMG));
        board[7][7].put(new Rook(1, board[7][7], WRIMG));
        board[0][1].put(new Knight(0, board[0][1], BNIMG));
        board[0][6].put(new Knight(0, board[0][6], BNIMG));
        board[7][1].put(new Knight(1, board[7][1], WNIMG));
        board[7][6].put(new Knight(1, board[7][6], WNIMG));
        board[0][2].put(new Bishop(0, board[0][2], BBIMG));
        board[0][5].put(new Bishop(0, board[0][5], BBIMG));
        board[7][2].put(new Bishop(1, board[7][2], WBIMG));
        board[7][5].put(new Bishop(1, board[7][5], WBIMG));
    
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccPiece());
                Wpieces.add(board[7-y][x].getOccPiece());
            }
        }
        checkChecker = new CheckChecker(this, Wpieces, Bpieces, wk, bk);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX, currY, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));
        if (sq.isOccupied()) {
            currPiece = sq.getOccPiece();
            if (currPiece.getColor()==0)return;  //cannot move black pieces (that's the ai's job)
            if (currPiece.getColor()==1&&!whiteTurn)return;             
            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));
        if (currPiece != null) {
            if (currPiece.getColor()==0)return;
            if (currPiece.getColor()==1&&!whiteTurn)return;
            ArrayList<Square> legalMoves = currPiece.getLegalMoves(this);
            movable = checkChecker.getAllowableSquares(whiteTurn);
            if(legalMoves.contains(sq)&&movable.contains(sq)&&checkChecker.tryMove(currPiece,sq)){ //can move if the move is legal, the piece can reach the square, and it doesnt put you in check
                sq.setDisplay(true);
                currPiece.move(sq);
                checkChecker.update();
                if (checkChecker.BlackInCheck()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    c.checkmate(0);
                } else if (checkChecker.WhiteInCheck()) {
                    currPiece = null;
                    repaint();
                    this.removeMouseListener(this);
                    this.removeMouseMotionListener(this);
                    c.checkmate(1);
                } else {
                    currPiece = null;
                    whiteTurn = !whiteTurn;
                    movable = checkChecker.getAllowableSquares(whiteTurn);
                }
                //THE AI PART
                while(!whiteTurn){
                    whiteTurn=AlphaBeta.minimaxRoot(SEARCH_DEPTH,this,true);
                    checkChecker.update();
                }
            } else {
                currPiece.getPosition().setDisplay(true);
                currPiece=null;
            }
        }
        repaint();  //safety repaint
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(whiteTurn){      //update coordinates for gui visuals
            currX = e.getX() - 24;
            currY = e.getY() - 24;
            repaint();
        }
    }



    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}