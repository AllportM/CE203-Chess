package Assignment2;

import java.util.*;
import static Assignment2.Team.*;

class AIPlayerController {
    TreeSet<PointsAndMoves> score;
    Board original;
    int difficulty = 3;
    boolean broke = false;

    public AIPlayerController()
    {
    }

    public PointsAndMoves getBestMove(Board board)
    {
        callMiniMax(board);
        return score.first();
    }

    private void callMiniMax(Board b)
    {
        Board copy = new Board(b);
        this.original = b;
        int i;
        if (copy.playersTurn.getPlayer() == Team.P1) i=0;
        else i = 1;
        Node root = new Node(copy);
        score = new TreeSet<>();
//        miniMax2(0, copy);
        miniMax(i,0, Integer.MIN_VALUE, Integer.MAX_VALUE, copy, root);
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
        if (depth == difficulty && player == b.getP1()) return returnAIBoardEval(b);
        if (depth == difficulty && player == b.getP2()) return returnAIBoardEval(b);
        if (player == b.getP2()) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        Set<PointsAndMoves> moves = new HashSet<>();
        for (Piece piece: player.getPlayerPieces())
        {
            for (Coord coord: player.getPieceMoves(piece))
            {
                moves.add(new PointsAndMoves(piece.getPosition(), coord));
            }

        }
        if (moves.size() == 0) System.out.println("no moves available");

        try {
            for (PointsAndMoves move : moves) {
                b.setAIMakingMove(true);
                b.considerMove(move.getOrigin());
                b.makeMove(move.getDestination());
                b.setAIMakingMove(false);
                int currentScore;
                if (pInt == 1) {
                    currentScore = miniMax(0, depth + 1, alpha, beta, b, newN);
                    move.setScore(currentScore);
                    scores.add(move);
                    temp = Math.max(temp, currentScore);
                    alpha = Math.max(alpha, temp);
                    if (depth == 0) {
                        score.add(move);
                    }
                } else if (pInt == 0) {
                    currentScore = miniMax(1, depth + 1, alpha, beta, b, newN);
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
            original.takeMove(newN.b);
            System.out.println("Null pointer");
        }
//        }
//
        return (player == b.getP2())? scores.first().getScore(): scores.last().getScore();
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
}
