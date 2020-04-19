/*  
    This file is designed to run a game of chess
    through the users terminal.

    To move a piece, you must enter it's coordinate (ex: "A1")
    with exactly one uppercase letter (A-H) and one integer (1-8)
    followed by the coordinate of the space you wish to move
    the piece to.

    WARNING: there are a small number of features yet to be implemented.
    
    The game is yet to be able to determine when a player has won
    or when the game is a draw; therefore, I have added the ability 
    to concede in order to terminate the program and declare a winner.

    One feature not yet implemented is capturing en passant.
    This move will never register as valid in the pawn pathing 
    and legal moving algorithms.

    The final work to be done is implementing a GUI.

    This program is entirely composed of logic I learned
    from CS180 class (my first time learning code), and, 
    therefore, I have only built this program using 
    what I learned entirely from that class.

    Things are likely not efficient. Things are likely completely wrong.
    I built this as a learning experience to teach myself the basics of
    Java and to have something fun I can play around with.

*/


import Objects.Other.Player;
import Objects.Other.Tile;
import Objects.Pieces.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class JavaChess {
    static int flicker = 0;
    static Scanner input = new Scanner(System.in);
    static String input1;
    static String input2;

    //game loop
    public static void main(String[] args) {
        Tile[][] board = new Tile[8][8];
        Piece[][] pieceMap = new Piece[8][8];

        Player player1 = new Player("player 1", "white");
        Player player2 = new Player("player 2", "black");
        Player currentPlayer;
        currentPlayer = player1;

        setBoard(board);
        setPieceMap(board, pieceMap);
        System.out.println();
        System.out.println("*Type 'Help' to display a list of commands.");

        do {
            displayGame(board, pieceMap);
            Piece x;
            Tile y;

            if (currentPlayer.getPlayerId().equals("player 1")) {
                try {

                    x = targetPiece(input, board, pieceMap, currentPlayer);
                    if (x == null && !input1.equals("Concede")){
                        currentPlayer = player2;
                        continue;
                    }else if (x == null){
                        System.out.println("Player 2 Wins!");
                        break;
                    }
                    y = targetTile(input, board, pieceMap, currentPlayer, x);
                    if (y == null) {
                        continue;
                    }

                    if (isCheck(y, board, pieceMap, currentPlayer)) {
                        movePieceToTile(x, y, board, pieceMap, currentPlayer);
                    } else {
                        System.out.println();
                        System.out.println("Move would result in your king being checkmated, try again.");
                        System.out.println();
                        continue;
                    }

                } catch (InputMismatchException e) {
                    System.out.println();
                    System.out.println("Invalid input. Enter the letter + number (ex: A1) of the piece you would like to move.");
                    displayGame(board, pieceMap);

                    input.next();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println();
                    System.out.println("Invalid input. Enter a valid coordinate.");
                    displayGame(board, pieceMap);
                }

                if (isCheckMate(board, pieceMap)) {
                    break;
                }

                currentPlayer = player2;

            } else {
                try {

                    x = targetPiece(input, board, pieceMap, currentPlayer);
                    if (x == null && !input1.equals("Concede")){
                        currentPlayer = player2;
                        continue;
                    }else if (x == null){
                        System.out.println("Player 1 Wins!");
                        break;
                    }
                    y = targetTile(input, board, pieceMap, currentPlayer, x);
                    if (y == null) {
                        continue;
                    }
                    //simulate move: if king is in check, return false
                    if (isCheck(y, board, pieceMap, currentPlayer)) {
                        movePieceToTile(x, y, board, pieceMap, currentPlayer);
                    } else {
                        System.out.println();
                        System.out.println("Move would result in your king being checkmated, try again.");
                        System.out.println();
                        continue;
                    }
                } catch (InputMismatchException e) {
                    System.out.println();
                    System.out.println("Invalid input. Enter the letter + number (ex: A1) of the tile you would like to move to.");
                    displayGame(board, pieceMap);

                    input.next();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println();
                    System.out.println("Invalid target. Enter a valid coordinate.");
                    displayGame(board, pieceMap);
                }

                if (isCheckMate(board, pieceMap)) {
                    break;
                }

                currentPlayer = player1;
            }

        } while (!isCheckMate(board, pieceMap) && !isDraw(board, pieceMap));

        input.close();
    }


    //sets the board, piece map, and prints them
    private static void setBoard(Tile[][] board) {

        int temp;
        int count = 1;
        for (int i = 0; i < board.length; i++) {
            temp = i + 1;
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
                board[i][j].setCoord(count++);
                board[i][j].setCoordId("" + ((char) (65 + j)) + temp);
                board[i][j].setRank(temp);
            }
        }
    }

    //sets pieceMap for simulation in isCheck method
    private static void setNewPieceMap(Tile[][] newBoard, Piece[][] newPieceMap, Tile[][] board, Piece[][] pieceMap){

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (pieceMap[i][j] != null) {
                    if (pieceMap[i][j].getPieceType().equals("Rook") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new Rook("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Rook") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new Rook("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Knight") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new Knight("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Knight") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new Knight("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Bishop") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new Bishop("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Bishop") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new Bishop("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Queen") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new Queen("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Queen") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new Queen("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("King") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new King("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("King") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new King("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Pawn") && pieceMap[i][j].getPieceColor().equals("white")) {
                        newPieceMap[i][j] = new Pawn("white");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    } else if (pieceMap[i][j].getPieceType().equals("Pawn") && pieceMap[i][j].getPieceColor().equals("black")) {
                        newPieceMap[i][j] = new Pawn("black");
                        newBoard[i][j].setOccupied(true);
                        newPieceMap[i][j].setStartCoord(newBoard[i][j].getCoord());
                    }
                }
            }
        }
    }


    //sets the pieceMap: a 2d array containing all of the pieces
    private static void setPieceMap(Tile[][] board, Piece[][] pieceMap) {

        for (int i = 0; i < pieceMap.length; i++) {
            for (int j = 0; j < pieceMap[i].length; j++) {
                if (board[i][j] == board[0][0] || board[i][j] == board[0][7]) {
                    pieceMap[i][j] = new Rook("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[0][1] || board[i][j] == board[0][6]) {
                    pieceMap[i][j] = new Knight("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[0][2] || board[i][j] == board[0][5]) {
                    pieceMap[i][j] = new Bishop("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[0][3]) {
                    pieceMap[i][j] = new Queen("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[0][4]) {
                    pieceMap[i][j] = new King("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (i == 1) {
                    pieceMap[i][j] = new Pawn("white");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (i == 6) {
                    pieceMap[i][j] = new Pawn("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[7][4]) {
                    pieceMap[i][j] = new King("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[7][3]) {
                    pieceMap[i][j] = new Queen("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[7][2] || board[i][j] == board[7][5]) {
                    pieceMap[i][j] = new Bishop("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[7][1] || board[i][j] == board[7][6]) {
                    pieceMap[i][j] = new Knight("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                } else if (board[i][j] == board[7][0] || board[i][j] == board[7][7]) {
                    pieceMap[i][j] = new Rook("black");
                    board[i][j].setOccupied(true);
                    pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                }
            }
        }
    }

    //print method that prints the board state to the terminal
    private static void displayGame(Tile[][] board, Piece[][] pieceMap) {
        char[][] gameDisplay = new char[8][8];
        int count = 1;

        System.out.println();
        for (int i = 0; i < gameDisplay.length; i++) {
            System.out.print("    " + (char) (64 + count++) + "\t");
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------------");

        count = 1;

        for (int i = 0; i < board.length; i++) {

            System.out.print(count++ + "|");

            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isOccupied()) {
                    if (j != 7) {
                        System.out.print("   " + pieceMap[i][j].getDisplay() + "\t|");
                    } else {
                        System.out.println("   " + pieceMap[i][j].getDisplay() + "\t|");
                    }
                } else {
                    if (j == 7) {
                        System.out.println("" + gameDisplay[i][j] + "\t|");
                    } else {
                        System.out.print("" + gameDisplay[i][j] + "\t|");

                    }
                }
            }
            System.out.println("-----------------------------------------------------------------");
        }
    }


    //check for game conditions
    private static boolean isCheck(Tile targetTile, Tile[][] board, Piece[][] pieceMap, Player currentPlayer) {
        //initialize king position
        Tile kingPos = null;
        Tile othKingPos = null;


        //create a copy of board and pieces to make the move
        Tile[][] boardCopy = new Tile[8][8];
        Piece[][] pieceMapCopy = new Piece[8][8];

        setBoard(boardCopy);
        setNewPieceMap(boardCopy, pieceMapCopy, board, pieceMap);

        Piece targetPieceCopy = targetPiece(input, boardCopy, pieceMapCopy, currentPlayer);
        Tile targetTileCopy = targetTile(input, boardCopy, pieceMapCopy, currentPlayer, targetPieceCopy);

        input1 = null;
        input2 = null;

        for (int i = 0; i < boardCopy.length; i++) {
            for (int j = 0; j < boardCopy[i].length; j++) {
                if (board[i][j].getCoord() == targetTile.getCoord()) {
                    targetTileCopy = boardCopy[i][j];
                }
            }
        }

        //make the move with the board and piece copies
        flicker = 1;
        movePieceToTile(targetPieceCopy, targetTileCopy, boardCopy, pieceMapCopy, currentPlayer);
        flicker = 0;

        //loop to save the king's location in copied game
        for (int i = 0; i < boardCopy.length; i++) {
            for (int j = 0; j < boardCopy[i].length; j++) {
                if (pieceMapCopy[i][j] != null) {
                    if (pieceMapCopy[i][j].getPieceType().equals("King")) {
                        if (currentPlayer.getPlayerColor().equals(pieceMapCopy[i][j].getPieceColor())) {
                            kingPos = boardCopy[i][j];
                        }else{
                            othKingPos = boardCopy[i][j];
                        }
                    }
                }
            }
        }

        //loop to find the location of all other enemy pieces in copied game
        //using king's location, find if a piece can legally check the king in copied game
        for (int i = 0; i < boardCopy.length; i++) {
            for (int j = 0; j < boardCopy[i].length; j++) {
                if (pieceMapCopy[i][j] != null && kingPos != null && othKingPos != null) {
                    //if the piece is an enemy piece, check if it can legally get to the king
                    if (!pieceMapCopy[i][j].getPieceColor().equals(currentPlayer.getPlayerColor())) {
                        //if it can legally move and path to the king's position, return false (king is not safe)
                        if (pieceMapCopy[i][j].isLegalMove(kingPos.getCoord())
                                && pieceMapCopy[i][j].isLegalPath(kingPos.getCoord(), boardCopy, pieceMapCopy)) {
                            return false;
                        }
                    }else{
                        if (pieceMapCopy[i][j].getPieceColor().equals(currentPlayer.getPlayerColor())){
                            if (pieceMapCopy[i][j].isLegalMove(othKingPos.getCoord())
                            && pieceMapCopy[i][j].isLegalPath(othKingPos.getCoord(), boardCopy, pieceMapCopy)){
                                System.out.println();
                                System.out.println("\t\t\t\t\tC H E C K !");
                                System.out.println();
                                for (int k = 0; k < boardCopy.length; k++){
                                    for (int l = 0; l < boardCopy[k].length; l++){
                                        if (othKingPos.getCoord() == pieceMap[k][l].getStartCoord()){
                                            pieceMap[k][l].setIsChecked(true);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //if it never returns false, then the king must be safe
        return true;
    }

    //placeholder methods for deciding when a checkmate or stalemate has been achieved
    private static boolean isCheckMate(Tile[][] board, Piece[][] pieceMap) {
        return false;
    }

    private static boolean isDraw(Tile[][] board, Piece[][] pieceMap) {
        return false;
    }

    private static boolean castle(Tile[][] board, Piece[][] pieceMap, Player currentPlayer) {
        /*
        >-prompt user to select a space to castle
        >-check if King and Rook selected isMoved == false
        >-check if King isChecked == false
        >-check if isLegalPath of Rook to King == true
        -check if isCheck of King on all spaces == false (yet to be implemented)
        >-if all are true then swap the two positions
         */


        System.out.println("Select the coordinate of the rook you would like to castle with: ");

        String rookTile = "";
        for (; ; ) {
            rookTile = input.next();

            if (currentPlayer.getPlayerColor().equals("white")) {
                if (rookTile.equals("A1") || rookTile.equals("H1")) {
                    break;
                }else if (rookTile.equals("X")){
                    return false;
                }
            } else if (currentPlayer.getPlayerColor().equals("black")){
                if (rookTile.equals("A8") || rookTile.equals("H8")) {
                    break;
                }else if (rookTile.equals("X")){
                    return false;
                }
            }

            System.out.println("Wrong coordinate. Try again.");
        }

        //checks if the rook has moved and if it can path to the king
        switch (rookTile) {
            case "A1":
                if (pieceMap[0][0].getIsMoved()) {
                    return false;
                }
                if (!pieceMap[0][0].isLegalPath(5, board, pieceMap)){
                    return false;
                }
                break;
            case "H1":
                if (pieceMap[0][7].getIsMoved()) {
                    return false;
                }
                if (!pieceMap[0][7].isLegalPath(5, board, pieceMap)){
                    return false;
                }
                break;
            case "A8":
                if (pieceMap[7][0].getIsMoved()) {
                    return false;
                }
                if (!pieceMap[7][0].isLegalPath(61, board, pieceMap)){
                    return false;
                }
                break;
            case "H8":
                if (pieceMap[7][7].getIsMoved()) {
                    return false;
                }

                if (!pieceMap[7][7].isLegalPath(61, board, pieceMap)){
                    return false;
                }
                break;
        }
        
        //checks if the King has been checked or has moved
        //if not, then castle based on parameters and return true
        if (currentPlayer.getPlayerColor().equals("white")){
            if (pieceMap[0][4].getIsMoved() || pieceMap[0][4].getIsChecked()) {
                return false;
            }

            if (rookTile.equals("A1")){
                Rook temp = (Rook) pieceMap[0][0];
                pieceMap[0][0] = pieceMap[0][4];
                pieceMap[0][4] = temp;

                pieceMap[0][0].setIsMoved(true);
                pieceMap[0][0].setStartCoord(board[0][0].getCoord());
                pieceMap[0][4].setStartCoord(board[0][4].getCoord());
                return true;
            }

            if (rookTile.equals("H1")){
                Rook temp = (Rook) pieceMap[0][7];
                pieceMap[0][7] = pieceMap[0][4];
                pieceMap[0][4] = temp;

                pieceMap[0][7].setIsMoved(true);
                pieceMap[0][7].setStartCoord(board[0][7].getCoord());
                pieceMap[0][4].setStartCoord(board[0][4].getCoord());
                return true;
            }

        }else {
            if (pieceMap[7][4].getIsMoved() || pieceMap[7][4].getIsChecked()){
                return false;
            }

            if (rookTile.equals("A8")){
                Rook temp = (Rook) pieceMap[7][0];
                pieceMap[7][0] = pieceMap[7][4];
                pieceMap[7][4] = temp;

                pieceMap[7][0].setIsMoved(true);
                pieceMap[7][0].setStartCoord(board[7][0].getCoord());
                pieceMap[7][4].setStartCoord(board[7][4].getCoord());
                return true;
            }

            if (rookTile.equals("H8")){
                Rook temp = (Rook) pieceMap[7][7];
                pieceMap[7][7] = pieceMap[7][4];
                pieceMap[7][4] = temp;

                pieceMap[7][7].setIsMoved(true);
                pieceMap[7][7].setStartCoord(board[7][7].getCoord());
                pieceMap[7][4].setStartCoord(board[7][4].getCoord());
                return true;
            }
        }
        return false;
    }

    /*
    -target a piece, check if it's valid
    -target a tile, check if it's valid
    -move the piece to the tile
    */
    private static Piece targetPiece(Scanner input, Tile[][] board, Piece[][] pieceMap, Player currentPlayer) {

        for (; ; ) {
            String fromTile;
            if (input1 == null){
                System.out.println();
                System.out.println(currentPlayer.getPlayerId() + ", select a piece: ");
                fromTile = input.next();
                input1 = fromTile;
            }else{
                fromTile = input1;
            }

            if (fromTile.equals("Help")){
                getMenuDisplay();
                input1 = null;
                continue;
            }

            if (fromTile.equals("Castle")){
                if (castle(board, pieceMap, currentPlayer)){
                    input1 = null;
                    return null;
                }
                System.out.println("Not a valid castle move. Retry or make another move instead.");
                input1 = null;
                continue;
            }

            if (fromTile.equals("Concede")){
                input1 = "Concede";
                return null;

            }

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (isValidTargetPiece(fromTile, board, currentPlayer, pieceMap, i, j)) {
                        return pieceMap[i][j];
                    }
                }
            }

            System.out.println();
            System.out.println("Invalid target. Try again");
            input1 = null;
            displayGame(board, pieceMap);
        }
    }

    private static boolean isValidTargetPiece(String fromTile, Tile[][] board, Player currentPlayer, Piece[][] pieceMap, int i, int j) {
        //checks that the coord entered by the player is the same as the tileID
        return (fromTile.equals(board[i][j].getCoordId()))
                //only valid if the space is occupied with a piece
                && (board[i][j].isOccupied())
                //only valid if the color of the piece matches the current player's color
                && (currentPlayer.getPlayerColor().equals(pieceMap[i][j].getPieceColor()));
    }

    private static Tile targetTile(Scanner input, Tile[][] board, Piece[][] pieceMap, Player currentPlayer, Piece targetPiece) {

        for (; ; ) {
            String moveToTargetTile;
            if (input2 == null) {
                System.out.println();
                System.out.println(currentPlayer.getPlayerId() + ", select a tile.");
                moveToTargetTile = input.next();
                input2 = moveToTargetTile;
            }else{
                moveToTargetTile = input2;
            }

            if (moveToTargetTile.equals("X")) {
                input1 = null;
                input2 = null;
                return null;

            }else if (moveToTargetTile.equals("Help")){
                getMenuDisplay();
                input2 = null;
                continue;

            } else {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        Piece piece = pieceMap[i][j];
                        if (isValidTile(board[i][j], moveToTargetTile, currentPlayer, piece)
                                && (targetPiece.isLegalMove(board[i][j].getCoord()))
                                && moveToTargetTile.equals(board[i][j].getCoordId())
                                && targetPiece.isLegalPath(board[i][j].getCoord(), board, pieceMap)) {
                            return board[i][j];

                        }
                    }
                }
            }
            System.out.println();
            System.out.println("Invalid target. Try again");
            input2 = null;
            displayGame(board, pieceMap);
        }
    }

    private static boolean isValidTile(Tile tile, String moveToTargetTile, Player currentPlayer, Piece piece) {
        if (tile.isOccupied()) {
            return !((piece.getPieceColor()).equals(currentPlayer.getPlayerColor()));
        }
        return tile.getCoordId().equals(moveToTargetTile);
    }

    private static void movePieceToTile(Piece targetPiece, Tile targetTile, Tile[][] board, Piece[][] pieceMap, Player currentPlayer) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if ((board[i][j]).isOccupied() && (pieceMap[i][j]) == targetPiece) {
                    pieceMap[i][j] = null;
                    board[i][j].setOccupied(false);
                }
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == targetTile) {
                    pieceMap[i][j] = targetPiece;
                    board[i][j].setOccupied(true);
                    targetPiece.setStartCoord(board[i][j].getCoord());
                    targetPiece.setIsMoved(true);
                    
                    //if piece is a pawn and made it to the other side of the board, player may make it into another piece
                    if (pieceMap[i][j].isEvolved(board[i][j].getCoord()) && flicker == 0) {
                        label:
                        for (; ; ) {
                            try {
                                System.out.println();
                                System.out.println("Type a piece you would like ('Knight,' 'Queen,' 'Rook,' or 'Bishop.'): ");

                                String var = input.next();
                                switch (var) {
                                    case "Queen":
                                        pieceMap[i][j] = new Queen(currentPlayer.getPlayerColor());
                                        pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                                        break label;
                                    case "Knight":
                                        pieceMap[i][j] = new Knight(currentPlayer.getPlayerColor());
                                        pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                                        break label;
                                    case "Rook":
                                        pieceMap[i][j] = new Rook(currentPlayer.getPlayerColor());
                                        pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                                        break label;
                                    case "Bishop":
                                        pieceMap[i][j] = new Bishop(currentPlayer.getPlayerColor());
                                        pieceMap[i][j].setStartCoord(board[i][j].getCoord());
                                        break label;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Couldn't read that, try again.");
                                System.out.println();

                                input.next();
                            }
                        }
                    }
                }
            }
        }
    }

    private static void getMenuDisplay(){
        System.out.println("\t\t\t\t\t List of Commands");
        System.out.println("\t\t\t-------------------------------------------------");
        System.out.println("\t\t\t Type 'X' to cancel an action");
        System.out.println("\t\t\t Type 'Castle' for the prompt to castle your king");
        System.out.println("\t\t\t Type 'Help' anytime to view this menu");
        System.out.println("\t\t\t Type 'Concede' to forfeit the game");
        System.out.println("\t\t\t-------------------------------------------------");
    }
}