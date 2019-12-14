package Assignment2;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static Assignment2.Team.*;

abstract class Player
{
    private List<Piece> playerPieces;
    private Team colour;
    private Team player;
    Team playerType;
    String pName;
    King king;
    boolean moving = false;
    Board board;
    private boolean isCastling = false;
    private boolean canCastle = false;
    private Coord kingCastleDestination;
    private Coord castleCastlingDestination;

    Player(Team colour, Team player, Board board)
    {
        playerPieces = new ArrayList<>();
        this.board = board;
        this.colour = colour;
        this.player = player; // P1 or P2
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

    private void placePiecesOnBoard(Board board)
    {
        for (Piece piece: playerPieces)
        {
            board.getTile(piece.position).setPiece(piece);
        }
    }


    abstract boolean makeMove(Tile tile);

    public void removeMovesPlacingKingInCheck(Set<Coord> moves, Piece piece, Coord destination)
    {
        Tile origin = board.getTile(piece.getPosition());
        Tile tileMovingTo = board.getTile(destination);
        Piece pieceTaken = null;
        if (tileMovingTo.isOccupied())
        {
            pieceTaken = tileMovingTo.getTilePiece();
            tileMovingTo.clearPiece();
        }
        origin.clearPiece();
        tileMovingTo.setPiece(piece);
        Coord kingOrigin = null;
        if (piece instanceof King)
        {
            kingOrigin = king.getPosition();
            king.setCoord(destination);
        }
//        System.out.println("King = " + king);
//        System.out.println("Kings position = " + king.getPosition());
        Player opp;
        if (piece.teamPiece == P1) opp = board.getP2();
        else opp = board.getP1();
        for (Piece oppPiece: opp.getPlayerPieces())
        {
//            System.out.println("Opp Piece " + oppPiece);
//            System.out.println("Opp position = " + oppPiece.getPosition());
//            System.out.println("Opp moves" + oppPiece.getValidMoves(board));
            if (oppPiece.getValidMoves(board).contains(king.getPosition()) && oppPiece != pieceTaken)
            {
                moves.remove(destination);
            }
        }
        tileMovingTo.clearPiece();
        if (pieceTaken != null)
        {
            tileMovingTo.setPiece(pieceTaken);
        }
        if (kingOrigin != null)
        {
            king.setCoord(kingOrigin);
        }
        origin.setPiece(piece);
    }


    HashSet<Coord> getPieceMoves(Piece piece)
    {
        // gets only valid moves that does not place king in check by simulating each pieces moves & resetting
        HashSet<Coord> moves = piece.getValidMoves(board);
        for (Coord move: piece.getValidMoves(board))
        {
            removeMovesPlacingKingInCheck(moves, piece, move);
        }
        // gets valid castling moves for castle
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

    public HashSet<Coord> getAvailableMovingPiecesCoords()
    {
        HashSet<Coord> moves = new HashSet<>();
        for (Piece piece: getPlayerPieces())
        {
            if (getPieceMoves(piece).size() > 0)
            {
                moves.add(piece.getPosition());
            }
        }
        return moves;
    }

    boolean outOfMoves()
    {
        HashSet<Coord> moves = new HashSet<>();
        for (Piece piece: playerPieces)
        {
            moves.addAll(getPieceMoves(piece));
        }
        return moves.size() == 0;
    }

    private boolean kingsCastleMoveBlocked(Coord move)
    {
        for (Piece piece: board.getOpponent().getPlayerPieces())
        {
            if (piece.getValidMoves(board).contains(move)) return true;
        }
        return false;
    }

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
        }

    }

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

    void clearPlayerPieces()
    {
        playerPieces = new ArrayList<>();
    }

    public boolean canCastle() {
        return canCastle;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public void setCastling(boolean castling) {
        isCastling = castling;
    }

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public Coord getCastleCastlingDestination() {
        return castleCastlingDestination;
    }

    public Coord getKingCastleDestination() {
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
class HumanPlayer extends Player
{
    private long startTime;
    private int secondsTotal = 0;
    private Team winStatus;

    HumanPlayer(Team colour, Team player, Board board)
    {
        super(colour, player, board);
        playerType = HUMAN;
    }

    HumanPlayer(Player player, Board board)
    {
        super(player, board);
        secondsTotal = ((HumanPlayer) player).secondsTotal;
        startTime = ((HumanPlayer) player).startTime;
    }

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

    void startTime()
    {
        startTime = System.currentTimeMillis();
    }

    void endTime()
    {
        secondsTotal += System.currentTimeMillis() - startTime;
    }

    boolean hasPiece(Tile t)
    {
        for (Piece piece: getPlayerPieces())
        {
            if (piece == t.getTilePiece()) return true;
        }
        return false;
    }

    public int getSecondsTotal() {
        return secondsTotal;
    }

    public void setWinStatus(Team winStatus) {
        this.winStatus = winStatus;
    }

    public Team getWinStatus() {
        return winStatus;
    }

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + getColour() + " and my name is " + pName +".";
    }
}

class AIPlayer extends Player
{
    private TreeSet<PointsAndMoves> score;
    private int difficulty = 3;
    private boolean broke = false;
    private Board brokeat;

    AIPlayer(Team colour, Team player, Board board)
    {
        super(colour, player, board);
        playerType = AI;
    }

    AIPlayer(AIPlayer player, Board board)
    {
        super(player, board);
    }

    public void makeMove()
    {
        makeMove(new Tile(new Coord(0,0)));
    }

    // Polymorphism, changes the AI's behaviour of inherited makeMove for dynamic binding
    public boolean makeMove(final Tile tile)
    {
        board.clearColouredTiles();
        PointsAndMoves move = getBestMove();
        if (broke) board.takeMove(brokeat);
        else {
            board.considerMove(move.getOrigin());
            board.makeMove(move.getDestination());
            // makes the ai's castle move, given board would only ever allow players turn to remain ai if player castling
            if (board.playersTurn == board.getP2() && board.playersTurn.isCastling())
            {
                board.makeMove(board.playersTurn.getKingCastleDestination());
            }
        }
        return true;
    }

    public PointsAndMoves getBestMove()
    {
        callMiniMax(board);
        return score.first();
    }

    private void callMiniMax(final Board b)
    {
        int i;
        if (b.playersTurn.getPlayer() == Team.P1) i=0;
        else i = 1;
        Node root = new Node(b);
        score = new TreeSet<>();
        miniMax(i, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, new Board(b), root);
    }

    private int miniMax(int pInt, int depth, int alpha, int beta, Board b, Node n)
    {
        Node newN = new Node(n, b);
        if (b.isGameOver() && b.getWinner() == b.getP1()) return -23000;
        if (b.isGameOver() && b.getWinner() == b.getP2()) return 23000;
        else if (b.isGameOver()) return 0;

        TreeSet<PointsAndMoves> scores = new TreeSet<>();
        int temp;
        Player player = b.getPlayersTurn();
        if (depth == difficulty) return returnAIBoardEval(b);
        if (player == b.getP2()) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        Set<PointsAndMoves> moves = new HashSet<>();
        if(!(player.isCastling()))
        {
            for (Piece piece : player.getPlayerPieces())
            {
                for (Coord coord : player.getPieceMoves(piece))
                {
                    moves.add(new PointsAndMoves(piece.getPosition(), coord));
                }

            }
        }
        // adds the only two moves for a king when castling
        else
        {
            moves.add(new PointsAndMoves(player.getKing().getPosition(), player.getKing().getPosition()));
            moves.add(new PointsAndMoves(player.getKing().getPosition(), player.getKingCastleDestination()));
            depth--; // given only 2 moves, can search by an additional depth at little to no loss of efficiency
        }
        if (moves.size() == 0) System.out.println("no moves available");

        try {
        for (PointsAndMoves move : moves) {
            b.setAIMakingMove(true);
            if (!player.isCastling()) b.considerMove(move.getOrigin());
            b.makeMove(move.getDestination());
            b.setAIMakingMove(false);
            int currentScore;
            if (pInt == 1) {
                // castling call, player 1's go again
                if (b.playersTurn.isCastling())
                {
                    currentScore = miniMax(1, depth + 1, alpha, beta, b, newN);
                }
                else
                {
                    currentScore = miniMax(0, depth + 1, alpha, beta, b, newN);
                }
                move.setScore(currentScore);
                scores.add(move);
                temp = Math.max(temp, currentScore);
                alpha = Math.max(alpha, temp);
                if (depth == 0) {
                    score.add(move);
                }
            } else if (pInt == 0) {
                // castling call, player 2's go again
                if (b.playersTurn.isCastling())
                {
                    currentScore = miniMax(0, depth + 1, alpha, beta, b, newN);
                }
                else
                {
                    currentScore = miniMax(1, depth + 1, alpha, beta, b, newN);
                }
                move.setScore(currentScore);
                scores.add(move);
                temp = Math.min(temp, currentScore);
                beta = Math.min(beta, temp);
            }
            b.revertMove();

            if (alpha >= beta) {
                break;
            }
        }
        }
        catch (NullPointerException e)
        {
            brokeat = newN.b;
            System.out.println("Null pointer");
        }
//        }
//
        return (player == b.getP2())? scores.first().getScore(): scores.last().getScore();
    }

    private int returnAIBoardEval(Board board)
    {
        int[] scores = evalBoard(board);
        return scores[1] - scores[0]; // returns p1 score less p2 score (ai's best score)
    }

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
        scores[0] = p1Score;
        scores[1] = p2Score;
        return scores;
    }

//    private void miniMax2(int depth, Board b)
//    {
//        int alpha = Integer.MIN_VALUE;
//        int beta = Integer.MAX_VALUE;
//
//        Player ai = b.getPlayersTurn();
//        // populates set containing moves
//        Set<PointsAndMoves> moves = new HashSet<>();
//        for (Piece piece: ai.getPlayerPieces())
//        {
//            for (Coord coord: ai.getPieceMoves(piece))
//            {
//                moves.add(new PointsAndMoves(piece.getPosition(), coord));
//            }
//        }
//
//        for (PointsAndMoves move: moves)
//        {
//            b.setAIMakingMove(true);
//            b.considerMove(move.getOrigin());
//            b.makeMove(move.getDestination());
//            b.setAIMakingMove(false);
//            int score = min(depth+1, b, alpha, beta);
//            move.setScore(score);
//            this.score.add(move);
//            b.revertMove();
//        }
//    }
//
//    private int min(int depth, Board b, int alpha, int beta)
//    {
//        if (depth == difficulty) return returnMinEval(b);
//        if (b.isGameOver() && b.getWinner() == b.getP1()) return -23000;
//        if (b.isGameOver() && b.getWinner() == b.getP2()) return 23000;
//        if (b.isGameOver()) return 0;
//
//        int lowest = Integer.MAX_VALUE;
//        Player human = b.getPlayersTurn();
//        Set<PointsAndMoves> moves = new HashSet<>();
//        for (Piece piece: human.getPlayerPieces())
//        {
//            for (Coord coord: human.getPieceMoves(piece))
//            {
//                moves.add(new PointsAndMoves(piece.getPosition(), coord));
//            }
//        }
//
//        for (PointsAndMoves move: moves)
//        {
//            b.setAIMakingMove(true);
//            b.considerMove(move.getOrigin());
//            b.makeMove(move.getDestination());
//            b.setAIMakingMove(false);
//            int score = max(depth+1, b, alpha, beta);
//            lowest = Math.min(lowest, score);
//            beta = Math.min(beta, lowest);
//            b.revertMove();
//            if (alpha > beta) break;
//        }
//        return beta;
//    }
//
//    private int max(int depth, Board b, int alpha, int beta)
//    {
//        if (depth == difficulty) return returnMinEval(b);
//        if (b.isGameOver() && b.getWinner() == b.getP1()) return -23000;
//        if (b.isGameOver() && b.getWinner() == b.getP2()) return 23000;
//        if (b.isGameOver()) return 0;
//
//        int highest = Integer.MIN_VALUE;
//        Player human = b.getPlayersTurn();
//        Set<PointsAndMoves> moves = new HashSet<>();
//        for (Piece piece: human.getPlayerPieces())
//        {
//            for (Coord coord: human.getPieceMoves(piece))
//            {
//                moves.add(new PointsAndMoves(piece.getPosition(), coord));
//            }
//        }
//
//        for (PointsAndMoves move: moves)
//        {
//            b.setAIMakingMove(true);
//            b.considerMove(move.getOrigin());
//            b.makeMove(move.getDestination());
//            b.setAIMakingMove(false);
//            int score = max(depth+1, b, alpha, beta);
//            highest = Math.max(highest, score);
//            alpha = Math.max(alpha, highest);
//            b.revertMove();
//            if (alpha > beta) break;
//        }
//        return alpha;
//    }

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + getColour() + " and my name is " + pName +".";
    }
}
