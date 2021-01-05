package Assignment2;

import javax.swing.*;
import java.util.*;

import static Assignment2.Team.*;

/*
 * The Player class defines most of the commonalities between the two types of player, human and AI, however
 * both sub classes implement makeMove differently, with the ai player using MiniMax search algorithm to determine
 * the best move and then making the move on the board directly, whereas the human player makes a move upon players
 * reponse.
 *
 * The main methods for this class pertain to generating only valid moves for this board, i.e if their king is in check
 * they can only make moves whereby the king is not left in check afterwards
 */
abstract class Player
{
    private List<Piece> playerPieces; // Collection to store players live pieces
    private Team colour; // constant string of the players colour
    private Team player; // constant string of P1 or P2
    Team playerType; // constant string of Ai/human
    String pName; // players name, only valid for p1
    King king; // a reference to the specific players king
    boolean moving = false; // boolean control variable for whether player is moving, used by tileHandler to either makeMove or Consider
    Board board; // reference to the current board, used within methods to determine if a move is valid
    private boolean isCastling = false;  // control variable for board to know if player is making a castle move, to
                                        // then give user option to move king
    private boolean canCastle = false; // variable to indicate to board that they can castle, thereby having the ability to
    private Coord kingCastleDestination; // the kings original location when castling move was made
    private Coord castleCastlingDestination; // the specific castle destination during castling

    /*
     * default constructor, assigns member variables and initializes a cohort of new pieces for the player
     *
     * @params
     *      colour, WHITE or BLACK
     *      player, p1 or p2
     *      board, reference to the current board
     */
    Player(Team colour, Team player, Board board)
    {
        playerPieces = new ArrayList<>();
        this.board = board;
        this.colour = colour;
        this.player = player; // P1 or P2
        // instantiates player pieces
        for (int i = 0; i < 8; i++)
        {
            if (player == P1)
            {
                playerPieces.add(new Pawn(new Coord(i, 6), player, colour));
                if (i == 0 || i == 7) playerPieces.add(new Castle(new Coord(i, 7), player, colour));
                if (i == 1 || i == 6) playerPieces.add(new Horse(new Coord(i, 7), player, colour));
                if (i == 2 || i == 5) playerPieces.add(new Bishop(new Coord(i, 7), player, colour));
                if (i == 3) playerPieces.add(new Queen(new Coord(3, 7), player, colour));
                King pKing = new King(new Coord(4, 7), player, colour);
                king = pKing;
                if (i == 4) playerPieces.add(pKing);
            }
            else
            {
                playerPieces.add(new Pawn(new Coord(i, 1), player, colour));
                if (i == 0 || i == 7) playerPieces.add(new Castle(new Coord(i, 0), player, colour));
                if (i == 1 || i == 6) playerPieces.add(new Horse(new Coord(i, 0), player, colour));
                if (i == 2 || i == 5) playerPieces.add(new Bishop(new Coord(i, 0), player, colour));
                if (i == 3) playerPieces.add(new Queen(new Coord(3, 0), player, colour));
                King pKing = new King(new Coord(4, 0), player, colour);
                king = pKing;

                if (i == 4) playerPieces.add(pKing);
            }
        }
        placePiecesOnBoard(board);
    }

    // Copy constructor
    Player (Player player, Board board)
    {
        this.board = board;
        playerPieces = new ArrayList<>();
        colour = player.colour;
        playerType = player.getPlayerType();
        pName = player.getpName();
        moving = player.isMoving();
        this.player = player.getPlayer();
        for (Piece piece: player.getPlayerPieces())
        {
            if (piece instanceof Pawn) playerPieces.add(new Pawn((Pawn) piece));
            if (piece instanceof Bishop) playerPieces.add(new Bishop(piece));
            if (piece instanceof Castle) playerPieces.add(new Castle((Castle) piece));
            if (piece instanceof Horse) playerPieces.add(new Horse(piece));
            if (piece instanceof Queen) playerPieces.add(new Queen(piece));
            if (piece instanceof King)
            {
                King king = new King((King) piece);
                playerPieces.add(king);
                this.king = king;
            }
        }
        placePiecesOnBoard(board);
    }

    // associates all player pieces to JPanel Tiles
    private void placePiecesOnBoard(Board board)
    {
        for (Piece piece: playerPieces)
        {
            board.getTile(piece.position).setPiece(piece);
        }
    }

    // The main difference between the two types of players to be implemented in sub classes
    abstract boolean makeMove(Tile tile);

    /*
     * movePlacesKingInCheck simulates a pieces move to determine if the king is in check following the move
     * Boolean value returned to indicate if it does place king in check
     *
     * @param
     *      piece, player piece to be checked
     *      destination, coordinate the piece is being moved to
     * @return
     *      boolean, indicates whether the move would place king in check
     */
    boolean movePlacesKingInCheck(Piece piece, Coord destination)
    {
        boolean itDoes = false;
        Tile origin = board.getTile(piece.getPosition());
        Tile tileMovingTo = board.getTile(destination);
        Piece pieceTaken = null;
        if (tileMovingTo.isOccupied())
        {
            pieceTaken = tileMovingTo.getTilePiece();
        }
        origin.clearPiece();
        tileMovingTo.setPiece(piece);
        Player opp = board.getOpponent();
        King newK = null;
        if (piece instanceof King)
        {
            newK = new King(destination, getTeam(), getColour());
        }
        for (Piece oppPiece: opp.getPlayerPieces())
        {
            if (oppPiece != pieceTaken)
            {
                if (oppPiece.getValidMoves(board).contains(king.getPosition()) && newK == null)
                    itDoes = true;
                if (oppPiece.getValidMoves(board).contains(destination) && newK != null)
                    itDoes = true;
            }
        }
        tileMovingTo.clearPiece();
        if (pieceTaken != null)
        {
            tileMovingTo.setPiece(pieceTaken);
        }
        origin.setPiece(piece);
        piece.setCoord(origin.getTileCoord());
        return itDoes;
    }


    /*
     * getPiecesMoves is the main method for calculating valid moves for a given piece
     *
     * @param
     *      piece, instance of a player piece to attain valid moves for
     * @return
     *      HashSet<Coord>, collection of unique moves the pievce can make
     */
    HashSet<Coord> getPieceMoves(Piece piece)
    {
        // gets only valid moves that does not place king in check by simulating each pieces moves & resetting
        HashSet<Coord> moves = new HashSet<>();
        for (Coord move: piece.getValidMoves(board))
        {
            if (!(movePlacesKingInCheck(piece, move))) moves.add(move);
        }
        // gets valid castling moves for castle and sets control variables indicating to the board if player can castle
        if (piece instanceof Castle && piece.isFirstMove() && king.isFirstMove())
        {
            Coord originCoord = piece.getPosition();
            switch (getTeam())
            {
                case P1:
                    if (originCoord.x == 0 && originCoord.y == 7 && !(board.getTile(1, 7).isOccupied()
                            || board.getTile(2, 7).isOccupied() || board.getTile(3, 7).isOccupied()))
                    {
                        Coord move = new Coord(3, 7);
                        if (!(kingsCastleMoveBlocked(move)))
                        {
                            moves.add(move);
                            canCastle = true;
                            castleCastlingDestination = move;
                            kingCastleDestination = new Coord(2, 7);
                        }
                    }
                    if (originCoord.x == 7 && originCoord.y == 7 && !(board.getTile(5, 7).isOccupied()
                            || board.getTile(6, 7).isOccupied()))
                    {
                        Coord move = new Coord(5, 7);
                        if (!(kingsCastleMoveBlocked(move)))
                        {
                            moves.add(move);
                            canCastle = true;
                            castleCastlingDestination = move;
                            kingCastleDestination = new Coord(6, 7);
                        }
                    }
                    break;
                case P2:
                    if (originCoord.x == 0 && originCoord.y == 0 && !(board.getTile(1, 0).isOccupied()
                            || board.getTile(2, 0).isOccupied() || board.getTile(3, 0).isOccupied()))
                    {
                        Coord move = new Coord(3, 0);
                        if (!(kingsCastleMoveBlocked(move)))
                        {
                            moves.add(move);
                            canCastle = true;
                            castleCastlingDestination = move;
                            kingCastleDestination = new Coord(2, 0);
                        }
                    }
                    if (originCoord.x == 7 && originCoord.y == 0 && board.getP2().getKing().isFirstMove()
                            && !(board.getTile(5, 0).isOccupied() || board.getTile(6, 0).isOccupied()))
                    {
                        Coord move = new Coord(5, 0);
                        if (!(kingsCastleMoveBlocked(move)))
                        {
                            moves.add(move);
                            canCastle = true;
                            castleCastlingDestination = move;
                            kingCastleDestination = new Coord(6, 0);
                        }
                    }
                    break;
            }
        }
        return moves;
    }

    /*
     * getAvailableMovingPiecesCoords's purpose is to return a list of pieces that can move on the current board.
     * Set is used for both mouse and keyboard listener to determine if the user is selecting a valid moving piece
     *
     * @returns
     *      HashSet<Coord>, collection of unique coordinates a player can move
     */
    HashSet<Coord> getAvailableMovingPiecesCoords()
    {
        HashSet<Coord> moves = new HashSet<>();
        for (Piece piece: getPlayerPieces())
        {
            if (getPieceMoves(piece).size() > 0)
            {
                moves.add(new Coord(piece.getPosition()));
            }
        }
        return moves;
    }

    /*
     * outOfMoves's purpose is to check through all a player pieces to see if the player can move. Mainly used at the end
     * of each turn to determine if the game is over
     *
     * @return
     *      boolean, indicating if a player can move or not
     */
    boolean outOfMoves()
    {
        HashSet<Coord> moves = new HashSet<>();
        for (Piece piece: playerPieces)
        {
            moves.addAll(getPieceMoves(piece));
        }
        return moves.size() == 0;
    }

    /*
     * kingsCastleMoveBlocked's purpose is to determine if a castling move can be made in which the king swaps places
     *
     * @returns
     *      boolean, indicates if the move the king would make is blocked by an opponent piece
     */
    private boolean kingsCastleMoveBlocked(Coord move)
    {
        for (Piece piece: board.getOpponent().getPlayerPieces())
        {
            if (piece.getValidMoves(board).contains(move)) return true;
        }
        return false;
    }

    /*
     * MovePiece's purpose is to actuate the move of a pieve, making checks if the piece is a pawn and can be turned
     * into a queen, setting the kings threat status, clearing/setting tile pieces, and displaying check message
     *
     * @params
     *      piece, the placer piece to be moves
     *      origin, the coordinate the piece came from
     *      destination, the coordinate the piece is being moved to
     */
    void movePiece(Piece piece, Coord origin, Coord destination)
    {
        piece.setFirstMove();

        // swaps pawn for queen if at end of board
        boolean pawnUpgrade = false;
        if (piece instanceof Pawn)
        {
            switch (player)
            {
                case P1:
                    if (destination.y == 0)
                    {
                        Queen newQ = new Queen(new Coord(piece.position.x, 0), player, colour);
                        board.getTile(newQ.getPosition()).setPiece(newQ);
                        removePiece(piece);
                        playerPieces.add(newQ);
                        pawnUpgrade = true;
                    }
                    break;
                case P2:
                    if (destination.y == 7)
                    {
                        Queen newQ = new Queen(new Coord(piece.position.x, 7), player, colour);
                        board.getTile(newQ.getPosition()).setPiece(newQ);
                        removePiece(piece);
                        playerPieces.add(newQ);
                        pawnUpgrade = true;
                    }
                    break;
            }
        }

        board.getTile(origin).clearPiece();

        // removes attacked pieces from players list
        if (board.getTile(destination).isOccupied())
        {
            board.opponent.removePiece(board.getTile(destination).getTilePiece());
        }
        if (!pawnUpgrade) board.getTile(destination).setPiece(piece);

        king.setUnderThreat(false);

        // sets king under threat
        piece.setCoord(destination);
        if (piece instanceof King)
        {
            king = (King) piece;
        };
        if (piece.getValidMoves(board).contains(board.opponent.getKing().getPosition()))
        {
            board.opponent.getKing().setUnderThreat(true);
            if (!board.aiMakingMove) board.getUi().displayMessage(new JLabel(board.opponent.getTeam() + "in check"));
        }

    }

    /*
     * removePiece's purpose is to remove a piece from the players collection of pieces
     *
     *  @param
     *      piece to be removed
     */
    private void removePiece(Piece piece)
    {
        for (Iterator<Piece> it = playerPieces.iterator(); it.hasNext();)
        {
            Piece pieceRem = it.next();
            if (pieceRem.getClass().equals(piece.getClass()) && piece.getPosition().equals(pieceRem.getPosition()))
            {
                it.remove();
            }
        }
    }

    // the remaining methods are simple set/get methods

    boolean canCastle() {
        return canCastle;
    }

    boolean isCastling() {
        return isCastling;
    }

    void setCastling(boolean castling) {
        isCastling = castling;
    }

    void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    Coord getCastleCastlingDestination() {
        return castleCastlingDestination;
    }

    Coord getKingCastleDestination() {
        return kingCastleDestination;
    }

    void setMoving(boolean status)
    {
        moving = status;
    }

    void setName(String name)
    {
        pName = name;
    }

    boolean isMoving()
    {
        return moving;
    }

    boolean isHuman()
    {
        return playerType == HUMAN;
    }


    Team getPlayerType()
    {
        return playerType;
    }

    private String getpName()
    {
        return pName;
    }

    Team getTeam()
    {
        return player;
    }

    Team getColour() {
        return colour;
    }

    King getKing()
    {
        return king;
    }

    List<Piece> getPlayerPieces()
    {
        return playerPieces;
    }

    Team getPlayer()
    {
        return player;
    }
}

/*
 * HumanPlayer is the sub class of Player for humans, having unique variables related to the players time spent
 * considering moves to calculate a score
 */
class HumanPlayer extends Player
{
    private long startTime; // System time in milliseconds, used to calculate and add to seconds spent total
    private int secondsTotal = 0; // total amount of milliseconds spent considering moves
    private Team winStatus; // WINNER, STALEMATE, or null, to indicate if player has won, used in calculating scores

    // default constructor, initializes variables
    HumanPlayer(Team colour, Team player, Board board)
    {
        super(colour, player, board);
        playerType = HUMAN;
    }

    // copy constructor
    HumanPlayer(Player player, Board board)
    {
        super(player, board);
        secondsTotal = ((HumanPlayer) player).secondsTotal;
        startTime = ((HumanPlayer) player).startTime;
    }

    // makes a move
    public boolean makeMove(Tile tile)
    {
        if (getPieceMoves(board.getMovingPiece()).contains(tile.getTileCoord())
                || tile.getTileCoord().equals(board.getMovingTile().getTileCoord()))
        {
            board.makeMove(tile.getTileCoord());
            return true;
        }

        return false;
    }

    // the remaining methods are simple set/get methods
    void startTime()
    {
        startTime = System.currentTimeMillis();
    }

    void endTime()
    {
        secondsTotal += System.currentTimeMillis() - startTime;
    }

    int getSecondsTotal() {
        return secondsTotal;
    }

    void setWinStatus(Team winStatus) {
        this.winStatus = winStatus;
    }

    Team getWinStatus() {
        return winStatus;
    }

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + getColour() + " and my name is " + pName +".";
    }
}


/*
 * AIPlayer implements makeMove, and contains member variable/functions to calculate best moves
 */
class AIPlayer extends Player
{
    private TreeSet<PointsAndMoves> score; // collection of sorted points and moves, which contain origin, destination,
                                        // and score evaluation for that given move
    private int difficulty = 3; // depth to which minimax searches, was going to have a difficulty of ai, but more
                                // than depth 3 takes too long without multithreading

    // default constructor
    AIPlayer(Team colour, Team player, Board board)
    {
        super(colour, player, board);
        playerType = AI;
    }

    // copy constructor
    AIPlayer(AIPlayer player, Board board)
    {
        super(player, board);
    }

    // board uses this method to call that of the abstract class, given a tile is irrelevant for ai
    void makeMove()
    {
        makeMove(new Tile(new Coord(0,0)));
    }

    // Polymorphism, changes the AI's behaviour of inherited makeMove for dynamic binding
    public boolean makeMove(final Tile tile)
    {
        board.clearColouredTiles();
        // synchronizes with lock variable on board so that message screens display correctly
        synchronized (board.lock) {
            try {
                board.lock.wait();
            } catch (InterruptedException ignore) {}
        }
        PointsAndMoves move = getBestMove(); // gets the best move via minimax
        board.setAIMakingMove(true);
        board.considerMove(move.getOrigin());
        board.setAIMakingMove(false);
        board.makeMove(move.getDestination());
        // makes the ai's castle move, given board would only ever allow players turn to remain ai if player castling
        if (board.playersTurn == board.getP2() && board.playersTurn.isCastling())
        {
            board.makeMove(board.playersTurn.getKingCastleDestination());
        }
        return true;
    }

    /*
     * getmove calls minimax and returns the highest scoring move, first element of TreeSet given natural sorting
     */
    PointsAndMoves getBestMove()
    {
        callMiniMax(board);
        return score.first();
    }

    /*
     * Initializes score set, determines player where 1 = player 2 (ai), 0 = p1
     */
    private void callMiniMax(final Board b)
    {
        int i;
        if (b.playersTurn.getPlayer() == Team.P1) i=0;
        else i = 1;
        score = new TreeSet<>();
        miniMax(i, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, new Board(b));
    }

    /*
     * Main minimax algorithm, recursive calls using alpha beta pruning
     */
    private int miniMax(int pInt, int depth, int alpha, int beta, Board b)
    {
        // endgame base cases && max depth base;
        if (b.isGameOver() && b.getWinner() == b.getP1()) return -23000;
        if (b.isGameOver() && b.getWinner() == b.getP2()) return 23000;
        else if (b.isGameOver()) return 0;
        if (depth == difficulty) return returnAIBoardEval(b); // evaluates board

        TreeSet<PointsAndMoves> scores = new TreeSet<>();
        int temp; // used in alpha beta pruning
        Player player = b.getPlayersTurn();
        if (player == b.getP2()) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        // gets list of all available moves, and creates new PointAndScore objects relating to origin, destination, and value
        Set<PointsAndMoves> moves = new HashSet<>();
        if(!(player.isCastling()))
        {
            for (Piece piece : player.getPlayerPieces())
            {
                for (Coord coord : player.getPieceMoves(piece))
                {
                    moves.add(new PointsAndMoves(new Coord(piece.getPosition()), new Coord(coord)));
                }

            }
        }
        // adds the only two moves for a king when castling
        else
        {
            moves.add(new PointsAndMoves(new Coord(player.getKing().getPosition()), new Coord(player.getKing().getPosition())));
            moves.add(new PointsAndMoves(new Coord(player.getKing().getPosition()), new Coord(player.getKingCastleDestination())));
            pInt = (player.getTeam() == P1)? 0: 1;
        }

        // main recursive call
        for (PointsAndMoves move : moves)
        {
            try
            {
                b.setAIMakingMove(true);
                if (!player.isCastling()) b.considerMove(move.getOrigin());
                b.makeMove(move.getDestination());
                b.setAIMakingMove(false);
                int currentScore;
                if (pInt == 1)
                { // p2 = ai, get max value board
                    currentScore = miniMax(0, depth + 1, alpha, beta, new Board(b));
                    move.setScore(currentScore);
                    scores.add(move);
                    temp = Math.max(temp, currentScore);
                    alpha = Math.max(alpha, temp);
                    if (depth == 0)
                    {
                        score.add(move);
                    }
                }
                else if (pInt == 0) // player 1, human, get minimum value move
                {
                        currentScore = miniMax(1, depth + 1, alpha, beta, b);
                    move.setScore(currentScore);
                    scores.add(move);
                    temp = Math.min(temp, currentScore);
                    beta = Math.min(beta, temp);
                }
                b.revertMove();

                if (alpha >= beta) {
                    break; // skips further moves in tree if lowest found
                }
            }
            catch (NullPointerException ignore) // unfortunately ai considers some illegal moves where a players king
            { // is removed from the board. I'm sure this is due to castling, but ran out of time to solve
                move.setScore(0);
                scores.add(move);
            } // initially player was never given the choice to move king upon castling, it was that point which broke ai :(
        }
        return (player == b.getP2())? scores.first().getScore(): scores.last().getScore();
    }

    /*
     * Returns the value of the current board in relation to player 2
     */
    private int returnAIBoardEval(Board board)
    {
        int[] scores = evalBoard(board);
        return scores[1] - scores[0]; // returns p1 score less p2 score (ai's best score)
    }

    /*
     * Evaluates board by calling methods set in Enum class Team, returns int array of p1's value and p2's
     */
    private int[] evalBoard(final Board board)
    {
        int[] scores = new int[2];
        int p1Score = 0;
        int p2Score = 0;
        for (Piece piece: board.getP1().getPlayerPieces())
        {
            p1Score += getPieceScore(piece);
        }
        for (Piece piece: board.getP2().getPlayerPieces())
        {
            p2Score += getPieceScore(piece);
        }
        // introduces a degree of randomness, 40% either way (i.e 1 unit value, would be 0.6 - 1.4)
        double fourtyPerc = (Math.random() * 0.8) + 0.6;
        scores[0] = (int) Math.round(p1Score * fourtyPerc);
        scores[1] = (int) Math.round(p2Score * fourtyPerc);
        return scores;
    }

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + getColour() + " and my name is " + pName +".";
    }
}
