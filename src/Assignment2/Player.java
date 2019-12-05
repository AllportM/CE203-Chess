package Assignment2;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Piece> playerPieces;
    Team colour;
    private Team player;
    private Team playerType;
    private String pName;
    private boolean checkStatus;
    private King king;

    Player(Team colour, Team player)
    {
        this.colour = colour;
        this.player = player; // P1 or P2
        playerPieces = new ArrayList<>();
    }

    void addPiece(Piece piece)
    {
        playerPieces.add(piece);
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

    void setCheckStatus()
    {
        checkStatus = true;
    }

    void clearCheckStatus() { checkStatus = false; }

    boolean getCheckStatus()
    {
        return checkStatus;
    }

    void removePiece(Piece piece)
    {
        playerPieces.remove(piece);
    }

    Team getPlayerType()
    {
        return playerType;
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

    @Override
    public String toString()
    {
        return "I am a " + playerType + " player and my colour is " + colour + " and my name is " + pName +".";
    }
}
