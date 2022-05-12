java chess game implementing alpha-beta pruned minimax AI  
  
alpha-beta-chess manual:  
  
This chess program has been written entirely in Java.  
CLASSES 		AND 		DESCRIPTIONS:  
Chess					controller for view and model  
Square					contains information regarding occupying pieces/piece movement+capture  
Board					logic model- manages user input (mouseEvents) and gameplay loop- where alphabeta functions are called  
CheckChecker				checks for checks, as well as providing allowable legal moves (moves that dont result in check)  
Piece					contains information regarding pieces- position, color, and image, as well as finding neighboring pieces on the row/col and diagonal  
Bishop,King,Knight,Pawn,Queen,Rook	extensions of the abstract class Piece. each overrides getLegalMoves() to return individualized legal moves given a board state  
AlphaBeta				contains functions for alphabeta pruned minimax search. returns best move given a board state. More info below  


AI  
the ai is alpha-beta pruned minimax, all functions are located in the AlphaBeta class.  
FUNCTIONS		AND		DESCRIPTIONS:  
minimaxRoot()				the method that is called to initialize minimax search with specified depth- recursively calls minimax() until it finds a satisfactory solution  
minimax()				called by minimaxRoot() to find best move given a current board  
evalBoard()				returns the total value of the current game's state, as defined by getPieceValue()  
getPieceValue()				returns hardcoded values of each piece to determine the total value of the board in evalBoard()  
  
the ai's methods are called on line 178 of Board.java to find the next move for black.  
to run the game, simply execute chess.jar using the Java Runtime Application or enter the following in the command line:  
	java -jar test.jar  
  
src included- compile by entering:  
	javac Chess.java  
  
create a new .jar archive by entering:  
	jar cvfe archive.jar Chess *.class  
  
