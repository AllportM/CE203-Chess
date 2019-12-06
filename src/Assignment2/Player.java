package Assignment2;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Piece> playerPieces;
    Team colour;
    private Team player;
    private Team playerType;
    private String pName;
    private King king;
    private boolean moving = false;
    private AIPlayer aiPlayer;

    Player(Team colour, Team player)
    {
        this.colour = colour;
        this.player = player; // P1 or P2
        playerPieces = new ArrayList<>();
    }

    Player(Player player)
    {
        playerPieces = new ArrayList<>();
        colour = player.colour;
        playerType = player.getPlayerType();
        pName = player.getpName();
        moving = isMoving();
        this.player = player.getPlayer();
        for (Piece piece: player.getPlayerPieces())
        {
            if (piece instanceof Pawn) playerPieces.add(new Pawn(piece));
            if (piece instanceof Bishop) playerPieces.add(new Bishop(piece));
            if (piece instanceof Castle) playerPieces.add(new Castle(piece));
            if (piece instanceof Horse) playerPieces.add(new Horse(piece));
            if (piece instanceof Queen) playerPieces.add(new Queen(piece));
            if (piece instanceof King)
            {
                King king = new King(piece);
                playerPieces.add(king);
                this.king = king;
            }
        }
    }

    void addPiece(Piece piece)
    {
        playerPieces.add(piece);
    }

    void setMoving(boolean status)
    {
        moving = status;
    }

    void setType(Team type)
    {
        playerType = type;
    }

    boolean hasPiece(Piece piece)
    {
        return playerPieces.contains(piece);
    }

    void setName(String name)
    {
        pName = name;
    }

    boolean isMoving()
    {
        return moving;
    }

    void removePiece(Piece piece)
    {
        playerPieces.remove(piece);
    }

    Team getPlayerType()
    {
        return playerType;
    }

    String getpName()
    {
        return pName;
    }

    void setKing(King king)
    {
        this.king= king;
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

    public AIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + colour + " and my name is " + pName +".";
    }
}
