import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private BufferedImage img;
    
    public Piece(int color, Square initSq, String img_file) {
        this.color = color;
        this.currentSquare = initSq;
        
        try {
            if (this.img == null) {
                this.img=ImageIO.read(Chess.class.getResource(img_file));
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
    }
    
    public boolean move(Square moveTo) {
        Piece occup = moveTo.getOccPiece();
        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else moveTo.capture(this);
        }
        currentSquare.removePiece();
        this.currentSquare = moveTo;
        currentSquare.put(this);
        return true;
    }
    
    public Square getPosition() {
        return currentSquare;
    }
    
    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }
    
    public int getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }
    
    public int[] getSideNeighbors(Square[][] board, int x, int y) {
        int lastYabove = 0;
        int lastXright = 7;
        int lastYbelow = 7;
        int lastXleft = 0;
        
        for (int i = 0; i < y; i++) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccPiece().getColor() != this.color) {
                    lastYabove = i;
                } else lastYabove = i + 1;
            }
        }

        for (int i = 7; i > y; i--) {
            if (board[i][x].isOccupied()) {
                if (board[i][x].getOccPiece().getColor() != this.color) {
                    lastYbelow = i;
                } else lastYbelow = i - 1;
            }
        }

        for (int i = 0; i < x; i++) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccPiece().getColor() != this.color) {
                    lastXleft = i;
                } else lastXleft = i + 1;
            }
        }

        for (int i = 7; i > x; i--) {
            if (board[y][i].isOccupied()) {
                if (board[y][i].getOccPiece().getColor() != this.color) {
                    lastXright = i;
                } else lastXright = i - 1;
            }
        }
        
        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};
        
        return occups;
    }
    
    public ArrayList<Square> getDiagNeigbors(Square[][] board, int x, int y) {
        ArrayList<Square> diagOcc = new ArrayList<Square>();
        
        int upLeftX = x - 1;
        int downLeftX = x - 1;
        int upRightX = x + 1;
        int downRightX = x + 1;
        int upLeftY = y - 1;
        int downLeftY = y + 1;
        int upRightY = y - 1;
        int downRightY = y + 1;
        
        while (upLeftX >= 0 && upLeftY >= 0) {
            if (board[upLeftY][upLeftX].isOccupied()) {
                if (board[upLeftY][upLeftX].getOccPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOcc.add(board[upLeftY][upLeftX]);
                    break;
                }
            } else {
                diagOcc.add(board[upLeftY][upLeftX]);
                upLeftY--;
                upLeftX--;
            }
        }
        
        while (downLeftX >= 0 && downLeftY < 8) {
            if (board[downLeftY][downLeftX].isOccupied()) {
                if (board[downLeftY][downLeftX].getOccPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOcc.add(board[downLeftY][downLeftX]);
                    break;
                }
            } else {
                diagOcc.add(board[downLeftY][downLeftX]);
                downLeftY++;
                downLeftX--;
            }
        }
        
        while (downRightX < 8 && downRightY < 8) {
            if (board[downRightY][downRightX].isOccupied()) {
                if (board[downRightY][downRightX].getOccPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOcc.add(board[downRightY][downRightX]);
                    break;
                }
            } else {
                diagOcc.add(board[downRightY][downRightX]);
                downRightY++;
                downRightX++;
            }
        }
        
        while (upRightX < 8 && upRightY >= 0) {
            if (board[upRightY][upRightX].isOccupied()) {
                if (board[upRightY][upRightX].getOccPiece().getColor() == this.color) {
                    break;
                } else {
                    diagOcc.add(board[upRightY][upRightX]);
                    break;
                }
            } else {
                diagOcc.add(board[upRightY][upRightX]);
                upRightY--;
                upRightX++;
            }
        }
        return diagOcc;
    }
    public abstract ArrayList<Square> getLegalMoves(Board b);
}