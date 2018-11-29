import java.util.*;

public class Board {
    private char [][] gameBoard; //our board
    private Move lastMove;  // last move
	private char lastLetterPlayed; last player
	public boolean isTerminal;   //terminal state
		lastMove = new Move(lastLetterPlayed);   // initialising last player
		//initialising board
        gameBoard = new char[8][8];
		for(int i =0; i<8;i++) {
			for(int j = 0; j<8; j++) {
                gameBoard[i][j] = 'o';
			}
		}
        gameBoard[3][3] = 'W';
        gameBoard[3][4] = 'B';
        gameBoard[4][3] = 'B';
        gameBoard[4][4] = 'W';
	}
	//method for receiving the board(not the initial)
    public Board(Board board) {
        lastMove = board.lastMove;
        lastLetterPlayed = board.lastLetterPlayed;
        gameBoard = new char[8][8];
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }
	//default values on the board
	public static int[][] sBOARD_VALUE = {
			{100, -1, 5, 2, 2, 5, -1, 100},
			{-1, -10,1, 1, 1, 1,-10, -1},
			{5 , 1,  1, 1, 1, 1,  1,  5},
			{2 , 1,  1, 0, 0, 1,  1,  2},
			{2 , 1,  1, 0, 0, 1,  1,  2},
			{5 , 1,  1, 1, 1, 1,  1,  5},
			{-1,-10, 1, 1, 1, 1,-10, -1},
			{100, -1, 5, 2, 2, 5, -1, 100}};
	// evaluation of a move based on the default values on which we add the pieces of a player(move is played on the
	//backround) and we subtrack the opponent pieces 
	public static int evaluateBoard(char[][] board,Move move) {
		int score = 0;
		for (int r = 0; r < 8; ++r) {
			for (int c = 0; c < 8; ++c) {
				if (board[r][c] == move.getPlayer())
					score += sBOARD_VALUE[r][c];
				else if (board[r][c] == move.getOpp())
					score -= sBOARD_VALUE[r][c];
			}
		}
		return score; //return value
	}

	//setting state as terminal
	public void setTerminal(){
		isTerminal = true;
	}
	//setting last move
	public void setLastMove(Move lastMove) {
		this.lastMove.setRow(lastMove.getRow());

		this.lastMove.setCol(lastMove.getCol());
		this.lastMove.setValue(lastMove.getValue());
	}
	//getter for last move
    public Move getLastMove()
    {
        return lastMove;
    }
	//setting last player that played
	public void setLastLetterPlayed(char lastLetterPlayed) {
		this.lastLetterPlayed = lastLetterPlayed;
	}
	//getter for last player
    public char getLastLetterPlayed() {
        return lastLetterPlayed;
    }
	//getting the board
    public char[][] getGameBoard() {
        return gameBoard;
    }
	//making move on the board
	public void makeMove(Move move) {

			gameBoard[move.getRow()][move.getCol()] = move.getPlayer();
			lastMove = move;//setting last move to move
			lastLetterPlayed = move.getPlayer(); //changing last player
	}
	//print method
	public void print(Board board) {
		System.out.println();
		System.out.println("* A B C D E F G H *");
		for(int i= 0; i<8; i++) {
			System.out.print(i+1+" ");
			for(int j = 0; j<8; j++) {
				System.out.print(board.gameBoard[i][j]+" ");
			}
			System.out.print(i+1+" ");
			System.out.println();
		}
		System.out.println("* A B C D E F G H *");
		System.out.println();

	}

	private static final int[] row_offset = {-1, -1, -1,  0,  0,  1,  1,  1};
	private static final int[] col_offset = {-1,  0,  1, -1,  1, -1,  0,  1};

	public static boolean isValidMove(char[][] board, char piece, int row, int col) {
		// check whether this square is empty
		if (board[row][col] != 'o')
			return false;
		char oppPiece = (piece == 'B') ? 'W' : 'B';
		boolean isValid = false;
		// check 8 directions(NW,N,NE,E,SE,S,SW,W)
		for (int i = 0; i < 8; ++i) {
			int curRow = row + row_offset[i];
			int curCol = col + col_offset[i];
			boolean hasOppPieceBetween = false;
			while (curRow >=0 && curRow < 8 && curCol >= 0 && curCol < 8) {
				if (board[curRow][curCol] == oppPiece)
					hasOppPieceBetween = true;
				else if ((board[curRow][curCol] == piece) && hasOppPieceBetween)//Finding the players piece so move=valid
				{
					isValid = true;
					break;
				}
				else
					break;
				curRow += row_offset[i];
				curCol += col_offset[i];
			}
			if (isValid)
				break;
		}

		return isValid; //return true for valid or false
	}
	//flip method based on code from isValid method
	public void flip(char[][] board, char piece, int row, int col) {
		board[row][col] = piece;
		// check 8 directions
		for (int i = 0; i < 8; ++i) {
			int curRow = row + row_offset[i];
			int curCol = col + col_offset[i];
			boolean hasOppPieceBetween = false;
			while (curRow >=0 && curRow < 8 && curCol >= 0 && curCol < 8) {
				// if empty square, break
				if (board[curRow][curCol] == 'o')
					break;
				if (board[curRow][curCol] != piece)
					hasOppPieceBetween = true;
				if ((board[curRow][curCol] == piece) && hasOppPieceBetween)
				{
					int effectPieceRow = row + row_offset[i];
					int effectPieceCol = col + col_offset[i];
					while (effectPieceRow != curRow || effectPieceCol != curCol)//flipping pieces
					{
						board[effectPieceRow][effectPieceCol] = piece;
						effectPieceRow += row_offset[i];
						effectPieceCol += col_offset[i];
					}
					break;
				}
				curRow += row_offset[i];
				curCol += col_offset[i];
			}
		}
	}

	//method used to determine whether the computer has any available moves in order to create children
	public boolean[][] FindMoves(Board board,char player){
		boolean[][] FMArray = new boolean[8][8];
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				if(isValidMove(board.getGameBoard(),player, i, j)) {
					FMArray[i][j]=true;
				}
			}
		}
		return FMArray;
	}
	//method used to determine whether the user has available move
	public boolean hasMoves(char player){
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				if(isValidMove(this.getGameBoard(),player, i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	//creating children
	public ArrayList<Board> getChildren(Board board,char letter) {
		ArrayList<Board> children = new ArrayList<>(); 
		boolean[][] finder = FindMoves(board,letter);//using find moves method
		Board iniBoard = board; // initial board
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				if(finder[i][j]){ // if move is valid create child
					Board child = new Board(iniBoard);
					Move move = new Move(letter,i,j,sBOARD_VALUE[i][j]);//create new move
					child.makeMove(move);
					child.flip(child.getGameBoard(),letter,i,j);//flip
					move.setValue(evaluateBoard(child.getGameBoard(),move));//setting new value based on the pieces
					children.add(child);//add in array list
				}
			}
		}
		return children;
	}

}


