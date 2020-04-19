package Objects.Pieces;

import Objects.Other.Tile;

public class King extends Piece {

    public King(String color){
        super(color);
        pieceType = "King";
        isChecked = false;

        if (color.equals("white")){
            display = '\u2654';
        }else
            display = '\u265A';
    }

    public char getDisplay(){
        return display;
    }

    @Override
    public String getPieceColor() {
        return color;
    }

    @Override
    public void setStartCoord(int startCoord) {
        this.startCoord = startCoord;
    }

    @Override
    public int getStartCoord() {
        return startCoord;
    }

    @Override
    public String getPieceType(){ return pieceType; }

    @Override
    public boolean isLegalMove(int finalCoord) {
        return (startCoord + 1 == finalCoord || startCoord - 1 == finalCoord || startCoord + 8 == finalCoord || startCoord - 8 == finalCoord
                || startCoord + 7 == finalCoord || startCoord + 9 == finalCoord || startCoord - 7 == finalCoord || startCoord - 9 == finalCoord);
    }

    @Override
    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    @Override
    public boolean getIsMoved() {
        return this.isMoved;
    }

    @Override
    public boolean isEvolved(int coord) {
        return false;
    }

    @Override
    public boolean isLegalPath(int finalCoord, Tile[][] board, Piece[][] pieceMap) {
        return true;
    }

    @Override
    public void setIsChecked(boolean isChecked){ this.isChecked = isChecked;}

    @Override
    public boolean getIsChecked() {
        return this.isChecked;
    }
}