package Assignment2;

import javafx.collections.transformation.SortedList;
import org.omg.CORBA.INTERNAL;

import java.util.*;

class AIPlayer {
    TreeSet<PointsAndMoves> score;
    int difficulty = 9;
    ChessUI ui;

    public AIPlayer(ChessUI ui)
    {
        this.ui = ui;
    }

    public PointsAndMoves getBestMove()
    {
        return score.first();
    }

    public void callMiniMax(Board b)
    {
        Board copy = new Board(b);
        copy.setAIEval();
        score = new TreeSet<>();
        miniMax(0, Integer.MIN_VALUE, Integer.MAX_VALUE, copy);
    }

    private int miniMax(int depth, int alpha, int beta, Board b)
    {
        if (b.isGameOver() && b.getWinner() == b.getP1()) return -23000;
        if (b.isGameOver() && b.getWinner() == b.getP2()) return 23000;
        else if (b.isGameOver()) return 0;

        List<Integer> scores = new ArrayList<>();

        int temp;
        Player player = b.getPlayersTurn();
        if (depth == difficulty && player == b.getP1()) return returnMinEval(b);
        if (depth == difficulty && player == b.getP2()) return returnMaxEval(b);
        if (player == b.getP2()) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        Set<PointsAndMoves> moves = new HashSet<>();
        for (Piece piece: player.getPlayerPieces())
        {
            if (piece.getValidMoves(b).size() != 0)
            {
                b.considerMove(b.board[piece.getPosition().y][piece.getPosition().x]);
                if (b.getPotentialMoves().size() != 0) {
                    b.getPotentialMoves().remove(piece.getPosition());
                    for (Coord destination : b.getPotentialMoves()) {
                        moves.add(new PointsAndMoves(piece.getPosition(), destination));
                    }
                }
            }
        }

        for(PointsAndMoves move: moves)
        {
            int currentScore;
            if (player == b.getP2())
            {
                b.considerMove(b.board[move.getOrigin().y][move.getOrigin().x]);
                b.makeMove(b.getTile(move.getDestination().x, move.getDestination().y));
                currentScore = miniMax(depth+1, alpha, beta, b);
                scores.add(currentScore);
                temp = Math.max(temp, currentScore);
                alpha = Math.max(alpha, temp);
                move.setScore(currentScore);
                if (depth == 0) score.add(move);
            }
            else if (player == b.getP1())
            {
                b.considerMove(b.getTile(move.getOrigin().x, move.getOrigin().y));
                b.makeMove(b.getTile(move.getDestination().x, move.getDestination().y));
                currentScore = miniMax(depth+1, alpha, beta, b);
                scores.add(currentScore);
                temp = Math.min(temp, currentScore);
                beta = Math.min(beta, temp);
            }
            b = new Board(b);
            if (alpha >= beta)
            {
                System.out.println("Number of nodes that have not been evaluated here : "+temp);
                break;
            }
        }
        return (player == b.getP2())? maxScores(scores): minScored(scores);
    }

    private int maxScores(List<Integer> scores)
    {
        Integer max = Integer.MIN_VALUE;
        for (Integer score: scores)
        {
            max = (score > max)? score: max;
        }
        return max;
    }

    private int minScored(List<Integer> scores)
    {
        Integer min = Integer.MAX_VALUE;
        for (Integer score: scores)
        {
            min = (score < min)? score: min;
        }
        return min;
    }

    private int returnMaxEval(Board board)
    {
        int[] scores = evalBoard(board);
        System.out.println(scores[1]);
        return scores[1];
    }

    private int returnMinEval(Board board)
    {
        int[] scores = evalBoard(board);
        System.out.println(scores[1]);
        return scores[1];
    }

    private int[] evalBoard(Board board)
    {
        int[] scores = new int[2];
        int p1Score = 0;
        int p2Score = 0;
        System.out.println(board.getP1().getPlayerPieces().size());
        System.out.println(board.getP2().getPlayerPieces().size());
        for (Piece piece: board.getP1().getPlayerPieces())
        {
            p1Score += pieceScore(piece);
        }
        for (Piece piece: board.getP2().getPlayerPieces())
        {
            p2Score += pieceScore(piece);
        }
        scores[0] = p1Score;
        scores[1] = p2Score;
        return scores;
    }

    private int pieceScore(Piece piece)
    {
        if (piece instanceof Pawn) return 100;
        if (piece instanceof Bishop) return 330;
        if (piece instanceof Castle) return 500;
        if (piece instanceof Horse) return 320;
        if (piece instanceof Queen) return 900;
        if (piece instanceof King) return 2000;
        else return 0;
    }
}
