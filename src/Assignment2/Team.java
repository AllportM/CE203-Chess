package Assignment2;

/*
 * Team enum class's purpose is to hold package specific constants, and static methods for evaluating a board
 *
 * Chess board evaluations have been taken from [1] for piece values, and positional board values
 *
 * [1]"Simplified Evaluation Function - Chessprogramming wiki", Chessprogramming.org, 2019. [Online].
 * Available: https://www.chessprogramming.org/Simplified_Evaluation_Function. [Accessed: 15- Dec- 2019].
 */
public enum Team {
    WHITE,
    BLACK,
    P1,
    P2,
    AI,
    HUMAN,
    WINNER,
    STALEMATE;

    public static int getPieceScore(Piece piece)
    {
        int score = 0;
        int posX = piece.getPosition().x;
        int posY = piece.getPosition().y;
        boolean player = piece.teamPiece == P1;
        if (piece instanceof Pawn)
        {
            score += (player)? P1PAWN[posY][posX]: P2PAWN[posY][posX];
            return score+200;
        }
        if (piece instanceof Bishop)
        {
            score += (player)? P1BISH[posY][posX]: P2BISHOP[posY][posX];
            return score + 330;
        }
        if (piece instanceof Castle)
        {
            score += (player)? P1CASTLE[posY][posX]: P2CASTLE[posY][posX];
            return score + 500;
        }
        if (piece instanceof Horse)
        {
            score += (player)? P1HORSE[posY][posX]: P2HORSE[posY][posX];
            return score + 320;
        }
        if (piece instanceof Queen)
        {
            score += (player)? P1QUEEN[posY][posX]: P2QUEEN[posY][posX];
            return score + 900;
        }
        if (piece instanceof King)
        {
            score += (player)? P1KING[posY][posX]: P2KING[posY][posX];
            return score + 2000;
        }
        return 0;
    }

    private final static int[][] P1PAWN =
            {
                {100,  100,  100,  100,  100,  100,  100,  100},
                {75, 75, 75, 75, 75, 75, 75, 75},
                {25, 25, 29, 29, 29, 29, 25, 25},
                {5,  5, 10, 25, 25, 10,  5,  5},
                {0,  0,  0, 20, 20,  0,  0,  0},
                {5, -5,-10,  0,  0,-10, -5,  5},
                {5, 10, 10,-20,-20, 10, 10,  5},
                {0,  0,  0,  0,  0,  0,  0,  0}
            };

    private final static int[][] P1HORSE =
        {
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-30,-30,-30,-30,-40,-50}
        };

    private final static int[][] P1BISH =
        {
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        };


    private final static int[][] P1CASTLE =
        {
                {0,  0,  0,  0,  0,  0,  0,  0},
                {5, 20, 20, 20, 20, 20, 20,  5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {0,  0,  0,  5,  5,  0,  0,  0}
        };

    private final static int[][] P1QUEEN =
        {
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                {-5,  0,  5,  5,  5,  5,  0, -5},
                { 0,  0,  5,  5,  5,  5,  0, -5},
                { -10,  5,  5,  5,  5,  5,  0,-10},
                { -10,  0,  5,  0,  0,  0,  0,-10},
                { -20,-10,-10, -5, -5,-10,-10,-20}
        };

    private final static int[][] P1KING =
        {
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                {20, 20,  0,  0,  0,  0, 20, 20},
                {20, 30, 10,  0,  0, 10, 30, 20}
        };

    private final static int[][] P2PAWN =
        {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {25, 25, 29, 29, 29, 29, 25, 25},
            {75, 75, 75, 75, 75, 75, 75, 75},
            {100,  100,  100,  100,  100,  100,  100,  100},
        };


    private final static int[][] P2HORSE =
        {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
        };

    private final static int[][] P2BISHOP =
        {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
        };


    private final static int[][] P2CASTLE =
        {
            {0,  0,  0,  5,  5,  20,  0,  0},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 5, 20, 20, 20, 20, 20, 20,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
        };


    private final static int[][] P2QUEEN=
        {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            { 0,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20},
        };

    private final static int[][] P2KING =
         {
            {20, 30, 10,  0,  0, 10, 60, 20},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
        };
}
