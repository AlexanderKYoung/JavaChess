package Objects.Pieces;

import Objects.Other.Tile;

public class Rook extends Piece {

    public Rook(String color){
        super(color);
        pieceType = "Rook";

        if (color.equals("white")){
            display = '\u2656';
        }else
            display = '\u265C';
    }

    @Override
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
    public String getPieceType() { return pieceType; }


    @Override
    public boolean isLegalMove(int finalCoord) {
        //checks to see if a point is in the last column and the other point lies on the horizontal vector
        if (finalCoord % 8 == 0 && !((Math.abs(startCoord - finalCoord) % 8) < 1)){
            return ((startCoord / 8) == ((finalCoord - 1) / 8));

        }else if (startCoord % 8 == 0 && !((Math.abs(startCoord - finalCoord) % 8) < 1)) {
            return (((startCoord - 1) / 8) == (finalCoord / 8));

        }else {
            //basic algorithm works for two points on the vertical vector (y axis)
            return ((Math.abs(startCoord - finalCoord) % 8) < 1
                    //basic algorithm that works for two points on the horizontal vector so long as no points lie on the last column
                    || startCoord / 8 == finalCoord / 8);
        }
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
                    if ((Math.abs(startCoord - finalCoord) % 8) < 1){
                        //determine which tiles are between start and final to test
                        int k = i+1;
                        while (board[k][j].getCoord() != startCoord && board[k][j].getCoord() != finalCoord) {
                            if (board[k][j].isOccupied()) {
                                return false;
                            } else {
                                k++;
                            }
                        }
                        return true;
                    } else if ((finalCoord % 8 == 0
                            && !((Math.abs(startCoord - finalCoord) % 8) < 1)
                            && ((startCoord / 8) == ((finalCoord - 1) / 8)))
                            || ((startCoord % 8 == 0)
                            && !((Math.abs(startCoord - finalCoord) % 8) < 1)
                            && ((startCoord - 1) /8) == (finalCoord /8))
                            || startCoord/8 == finalCoord/8) {
                        int l = j+1;
                        while (board[i][l].getCoord() != startCoord && board[i][l].getCoord() != finalCoord){
                            if (board[i][l].isOccupied()){
                                return false;
                            }else{
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
