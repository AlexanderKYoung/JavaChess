package Objects.Pieces;

import Objects.Other.Tile;

public abstract class Piece {
    String color;
    char display;
    int startCoord;
    boolean isMoved;
    boolean isChecked;
    String pieceType;

    //constructor method
    Piece(String color){
        this.color = color;
    }

    Piece(String color, String pieceType){
        this.color = color;
        this.pieceType = pieceType;
    }

    //returns the image of the piece
    public abstract char getDisplay();

    //returns the color of a piece
    public abstract String getPieceColor();

    public abstract String getPieceType();

    //resets the current coordinate of a piece
    public abstract void setStartCoord(int startCoord);

    //returns the current coordinate of a piece
    public abstract int getStartCoord();

    //restricts the movement of a piece
    public abstract boolean isLegalMove(int finalCoord);

    //toggles the boolean isMoved to determine if Castling is still valid and if a Pawn can move two spaces
    public abstract void setIsMoved(boolean isMoved);

    public abstract boolean getIsMoved();

    public abstract void setIsChecked(boolean isChecked);

    public abstract boolean getIsChecked();

    //to determine if a Pawn transforms into a Queen
    public abstract boolean isEvolved(int coord);

    //detects piece collision
    public abstract boolean isLegalPath(int finalCoord, Tile[][] board, Piece[][] pieceMap);
}
