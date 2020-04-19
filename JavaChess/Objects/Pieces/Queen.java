package Objects.Pieces;

import Objects.Other.Tile;

public class Queen extends Piece {

    public Queen(String color) {
        super(color);
        pieceType = "Queen";

        if (color.equals("white")) {
            display = '\u2655';
        } else
            display = '\u265B';
    }

    @Override
    public char getDisplay() {
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

        if ((Math.abs(startCoord - finalCoord) % 7) == 0 || (Math.abs(startCoord - finalCoord) % 9) == 0) {
            return true;
        } else if (finalCoord % 8 == 0 && !((Math.abs(startCoord - finalCoord) % 8) < 1)) {
            return ((startCoord / 8) == ((finalCoord - 1) / 8));

        } else if (startCoord % 8 == 0 && !((Math.abs(startCoord - finalCoord) % 8) < 1)) {
            return (((startCoord - 1) / 8) == (finalCoord / 8));

        } else return (Math.abs(startCoord - finalCoord) % 8) < 1 || startCoord / 8 == finalCoord / 8;
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //once the loop encounters either the start or final coord, begin searching for the direction
                if (board[i][j].getCoord() == startCoord || board[i][j].getCoord() == finalCoord) {
                    //vertical vector check
                    if ((Math.abs(startCoord - finalCoord) % 8) < 1) {
                        //determine which tiles are between start and final to test
                        int k = i + 1;
                        while (board[k][j].getCoord() != startCoord && board[k][j].getCoord() != finalCoord) {
                            if (board[k][j].isOccupied()) {
                                return false;
                            } else {
                                k++;
                            }
                        }
                        return true;
                        //horizontal vector checks
                    } else if ((finalCoord % 8 == 0
                            && !((Math.abs(startCoord - finalCoord) % 8) < 1)
                            && ((startCoord / 8) == ((finalCoord - 1) / 8)))
                            || ((startCoord % 8 == 0)
                            && !((Math.abs(startCoord - finalCoord) % 8) < 1)
                            && ((startCoord - 1) / 8) == (finalCoord / 8))
                            || startCoord / 8 == finalCoord / 8) {
                        int l = j + 1;
                        while (board[i][l].getCoord() != startCoord && board[i][l].getCoord() != finalCoord) {
                            if (board[i][l].isOccupied()) {
                                return false;
                            } else {
                                l++;
                            }
                        }
                        return true;
                    } else if (Math.abs(startCoord - finalCoord) % 7 == 0){
                        int k = i+1;
                        int l = j-1;
                        while (board[k][l].getCoord() != startCoord && board[k][l].getCoord() != finalCoord){
                            if (board[k][l].isOccupied()){
                                return false;
                            }else{
                                k++;
                                l--;
                            }
                        }
                        return true;
                    }else if (Math.abs(startCoord- finalCoord) % 9 == 0){
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
            }
        }
        return false;
    }
}