import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class Square extends JComponent {
    private Board b;
    
    private final int color;
    private Piece occupyingPiece;
    private boolean dispPiece;
    
    private int xPos;
    private int yPos;
    
    public Square(Board b, int c, int xNum, int yNum) {
        
        this.b = b;
        this.color = c;
        this.dispPiece = true;
        this.xPos = xNum;
        this.yPos = yNum;
        
        
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    
    public int getColor() {
        return this.color;
    }
    
    public Piece getOccPiece() {
        return occupyingPiece;
    }
    
    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }
    
    public int getXPos() {
        return this.xPos;
    }
    
    public int getYPos() {
        return this.yPos;
    }
    
    public void setDisplay(boolean v) {
        this.dispPiece = v;
    }
    
    public void put(Piece p) {
        this.occupyingPiece = p;
        p.setPosition(this);
    }
    
    public Piece removePiece() {
        Piece p = this.occupyingPiece;
        this.occupyingPiece = null;
        return p;
    }
    
    public void capture(Piece p) {
        Piece k = getOccPiece();
        if (k.getColor() == 0) b.Bpieces.remove(k);
        if (k.getColor() == 1) b.Wpieces.remove(k);
        this.occupyingPiece = p;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.color == 1) {
            g.setColor(new Color(221,192,127));
        } else {
            g.setColor(new Color(101,67,33));
        }
        
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        
        if(occupyingPiece != null && dispPiece) {
            occupyingPiece.draw(g);
        }
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + xPos;
        result = prime * result + yPos;
        return result;
    }
    
}