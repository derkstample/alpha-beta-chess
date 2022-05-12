import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chess {
    private JFrame frame;
    private Board board;

    public static void main(String[] args){
        new Chess();
    }

    public Chess() {
        frame = new JFrame("chess");
        frame.setLayout(new BorderLayout(20, 20)); //using borderlayout cause the buttons are easier
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setup the board
        this.board = new Board(this);
        frame.add(board, BorderLayout.CENTER);
        //setup the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));
        JButton quit = new JButton("quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        JButton restart = new JButton("restart");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Chess(); // new game
                frame.dispose(); // kill the old game
            }
        });
        JButton instructions = new JButton("instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "you are white, the ai is black. move by clicking and dragging.\nyou cannot play an illegal move (but neither can the ai, so its fair)\ngame ends in checkmate, or when you quit\nthe ai is based on the alpha-beta minimax search tree.\nbasically, take all legal moves and optimize your position after n many moves.");
            }
        });
        buttons.add(instructions);
        buttons.add(restart);
        buttons.add(quit);
        buttons.setPreferredSize(buttons.getMinimumSize());
        frame.add(buttons, BorderLayout.SOUTH); //see, told you borderlayout made this part easy
        //pretty it up
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setSize(frame.getPreferredSize());
        frame.pack();
        frame.setVisible(true);
    }

    public void checkmate(int c){
        if (c == 0) {
            int n = JOptionPane.showConfirmDialog(frame,"checkmate- white wins\nnew game? choosing \"no\" lets you look at the final board","white wins",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                new Chess();
                frame.dispose();
            }
        } else {
            int n = JOptionPane.showConfirmDialog(frame,"checkmate- black wins\nnew game? choosing \"no\" lets you look at the final board","black wins",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                new Chess();
                frame.dispose();
            }
        }
    }
}