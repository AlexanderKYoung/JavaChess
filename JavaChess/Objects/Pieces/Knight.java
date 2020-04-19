package Objects.Pieces;

import Objects.Other.Tile;

public class Knight extends Piece {

    public Knight(String color){
        super(color);
        pieceType = "Knight";

        if (color.equals("white")){
            display = '\u2658';
        }else{
            display = '\u265E';
        }
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
        return ((startCoord - 17 == finalCoord) || (startCoord - 15 == finalCoord) || (startCoord - 10 == finalCoord) || (startCoord - 6 == finalCoord)
                || (startCoord + 17 == finalCoord) || (startCoord + 15 == finalCoord) || (startCoord + 10 == finalCoord) || (startCoord + 6 == finalCoord));
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
    public void setIsChecked(boolean isChecked) {

    }

    @Override
    public boolean getIsChecked() {
        return false;
    }

    @Override
    public boolean isEvolved(int coord) {
        return false;
    }

    @Override
    public boolean isLegalPath(int finalCoord, Tile[][] board, Piece[][] pieceMap) {
        return true;
    }
}
