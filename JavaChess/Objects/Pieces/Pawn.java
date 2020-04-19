package Objects.Pieces;

import Objects.Other.Tile;

public class Pawn extends Piece{

    public Pawn(String color){
        super(color);
        pieceType = "Pawn";

        if (color.equals("white")) {
            display = '\u2659';
        }else{
            display = '\u265F';
        }
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
        if (isMoved && getPieceColor().equals("white")) {
            return startCoord + 8 == finalCoord
                    || startCoord + 7 == finalCoord || startCoord + 9 == finalCoord;

        }else if (isMoved && getPieceColor().equals("black")){
            return startCoord - 8 == finalCoord
                    || startCoord - 7 == finalCoord || startCoord - 9 == finalCoord;

        }else if (!isMoved && getPieceColor().equals("white")){
            return startCoord + 8 == finalCoord || startCoord + 16 == finalCoord
                    || startCoord + 7 == finalCoord || startCoord + 9 == finalCoord;

        }else if (!isMoved && getPieceColor().equals("black")) {
            return startCoord - 8 == finalCoord || startCoord - 16 == finalCoord
                    || startCoord - 7 == finalCoord || startCoord - 9 == finalCoord;
        }
        return false;
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
    public boolean isEvolved(int finalCoord) {
        if (getPieceColor().equals("white") && finalCoord > 56){
            return true;
        }else return getPieceColor().equals("black") && finalCoord < 9;
    }

    @Override
    public boolean isLegalPath(int finalCoord, Tile[][] board, Piece[][] pieceMap) {
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if (board[i][j].getCoord() == startCoord && this.getPieceColor().equals("white")){
                    if (Math.abs(startCoord - finalCoord) == 8){
                        int k = i+1;
                        return !board[k][j].isOccupied();
                    }
                    if (Math.abs(startCoord - finalCoord) == 16){
                        int k = i+1;
                        while (board[k][j].getCoord() != finalCoord+8){
                            if (board[k][j].isOccupied()){
                                return false;
                            }else{
                                k++;
                            }
                        }
                        return true;
                    }else if (Math.abs(startCoord - finalCoord) % 7 == 0){
                        int k = i+1;
                        int l = j-1;
                        return board[k][l].isOccupied() && !pieceMap[k][l].getPieceColor().equals("white");
                    }else if (Math.abs(startCoord - finalCoord) % 9 == 0){
                        int k = i+1;
                        int l = j+1;
                        return board[k][l].isOccupied() && !pieceMap[k][l].getPieceColor().equals("white");
                    }
                }else if (board[i][j].getCoord() == startCoord && this.getPieceColor().equals("black")){
                    if (Math.abs(startCoord - finalCoord) == 8) {
                        int k = i - 1;
                        return !board[k][j].isOccupied();
                    }
                    if (Math.abs(startCoord-finalCoord) == 16) {
                        int k = i - 1;
                        while (board[k][j].getCoord() != finalCoord - 8) {
                            if (board[k][j].isOccupied()) {
                                return false;
                            } else if (board[k][j] == null) {
                                return true;
                            } else {
                                k--;
                            }
                        }
                        return true;
                    }else if (Math.abs(startCoord - finalCoord) % 9 == 0){
                        int k = i-1;
                        int l = j-1;
                        return board[k][l].isOccupied() && !pieceMap[k][l].getPieceColor().equals("black");
                    }else if (Math.abs(startCoord - finalCoord) % 7 == 0){
                        int k = i-1;
                        int l = j+1;
                        return board[k][l].isOccupied() && !pieceMap[k][l].getPieceColor().equals("black");
                    }
                }
            }
        }
        return true;
    }
}
