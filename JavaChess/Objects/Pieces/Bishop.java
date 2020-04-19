package Objects.Pieces;

import Objects.Other.Tile;

public class Bishop extends Piece {

    public Bishop(String color){
        super(color);
        pieceType = "Bishop";

        if (color.equals("white")) {
            display = '\u2657';
        }else
            display = '\u265D';
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
        return ((Math.abs(startCoord - finalCoord) % 7) == 0 || (Math.abs(startCoord - finalCoord) % 9) == 0);
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
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                //once the loop encounters either the start or final coord, begin searching for the direction
                if (board[i][j].getCoord() == startCoord || board[i][j].getCoord() == finalCoord) {
                    //need to determine which direction the piece is moving using the finalCoord and startCoord relationship
                    if (Math.abs(startCoord - finalCoord) % 7 == 0) {
                        //determine which tiles are between start and final to test
                        int k = i+1;
                        int l = j-1;
                        //loop to check each tile between each position
                        //ends once the end of the vector is reached (start or final coord)
                        while (board[k][l].getCoord() != startCoord && board[k][l].getCoord() != finalCoord){
                            if (board[k][l].isOccupied()) {
                                return false;
                            }else{
                                k++;
                                l--;
                            }
                        }
                        return true;

                    } else if (Math.abs(startCoord - finalCoord) % 9 == 0) {
                        int k = i+1;
                        int l = j+1;
                        while (board[k][l].getCoord() != startCoord && board[k][l].getCoord() != finalCoord){
                            if (board[k][l].isOccupied()){
                                return false;
                            }else{
                                k++;
                                l++;
                            }
                        }
                        return true;
                    }
                }
                //then need to check board[i][j].isOccupied for each Tile in the vector between the two coords
                //if all Tiles return false, then return true
            }
        }
        return false;
    }
}
