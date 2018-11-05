package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private Piece[][] board = new Piece[8][8];
    private Scanner s = new Scanner(System.in);
    private Color currentTurn = Color.white;
    private Color winner = Color.none;
    private boolean verbose;

    private Game(boolean verbose){
        this.verbose = verbose;
    }

    private void populate(){
        for(int x = 0;x<board.length;x++){
            if(x%2 == 0){
                placePiece(Color.white,x,1);
                placePiece(Color.black,x,5);
                placePiece(Color.black,x,7);
            }else{
                placePiece(Color.white,x,0);
                placePiece(Color.white,x,2);
                placePiece(Color.black,x,6);
            }
        }
    }
    private Color checkWin() {
        int numBlack = 0;
        int numWhite = 0;
        for (Piece[] x : board) {
            for (Piece p : x) {
                if (p != null) {
                    if (p.getColor() == Color.black) {
                        numBlack += 1;
                    }
                    if (p.getColor() == Color.white) {
                        numWhite += 1;
                    }
                }
            }
        }
        if(verbose){
            System.out.println("Alive black pieces: " + numBlack);
            System.out.println("Alive white pieces: " + numWhite);
        }

        if (numBlack == 0) {
            return Color.white;
        }
        if (numWhite == 0) {
            return Color.black;
        }
        return Color.none;
    }
    
    private void placePiece(Color color,int x, int y){
        Piece p = new Piece(color,x,y);
        board[x][y] = p;
    }
    private void placePiece(Piece p,int x, int y){ board[x][y] = p; }
    private void removePiece(Piece p){ board[p.getX()][p.getY()] = null; }
    private void updatePiece(Piece p, int x, int y){
        p.setX(x);
        p.setY(y);
        if(p.getColor() == Color.white){
            if(y == 7){
                System.out.println("White piece has turned into king!");
                p.setKing(true);
            }
        }else{
            if(y == 0){
                System.out.println("Black piece has turned into king!");
                p.setKing(true);
            }
        }
    }

    private boolean[] movePiece(Piece p, int x, int y){
        if(isValidMove(p,x,y)){
            boolean j = eatPiece(p,x,y);
            removePiece(p);
            updatePiece(p,x,y);

            placePiece(p,x,y);
            return new boolean[]{true,j};
        }else{
            return new boolean[]{false,false};
        }
    }

    private int getMid(int a, int b) {return (a + b) / 2;}

    private boolean eatPiece(Piece p, int x, int y){
        if(isJump(p.getX(), p.getY(), x, y)){

            int midX = getMid(p.getX(),x);
            int midY = getMid(p.getY(),y);
            Piece target = board[midX][midY];
            if(verbose){
                System.out.println("Jump detected");
                System.out.println("Ate " + target.getColor() +" piece at (" + midX +","+midY+")");
            }
            removePiece(target);
            return true;
        }
        return false;
    }

    private boolean isJump(int x1, int y1, int x2, int  y2){
        return (Math.abs(x2-x1) == 2 && Math.abs(y2-y1) == 2);
    }

    private boolean isValidJump(Piece p, int x, int y){
        int midX = getMid(p.getX(),x);
        int midY = getMid(p.getY(),y);
        return (board[midX][midY] != null && board[midX][midY].getColor() != p.getColor());
    }

    private boolean isValidMove(Piece p, int x, int y){
        if (x<0 || x>7 || y<0 || y>7 ){
            System.out.println("Selected spot outside board.");
            return false;
        }
        if(!isDiag(p.getX(), p.getY(), x, y)){
            System.out.println("Not a valid move: non-diagonal.");
            return false;
        }
        if(spotTaken(x, y)){
            System.out.println("Not a valid move: spot taken by another piece.");
            return false;
        }
        if(!p.isKing()){
            if(!isForwardMovement(p, x, y)){
                System.out.println("Not a valid move: non-king piece must move forwards.");
                return false;
            }
        }
        if(isJump(p.getX(), p.getY(), x, y) && !isValidJump(p,x,y)){
            System.out.println("Not a valid jump - no piece/incorrect color.");
            return false;
        }
        if (verbose) {System.out.println("Valid move.");}
        return true;
    }
    private boolean spotTaken(int x, int y){ return(board[x][y] != null); }

    private boolean isForwardMovement(Piece p, int x, int y){
        if(p.getColor() == Color.black){
            return(y < p.getY());
        }else{
            return(y > p.getY());
        }
    }
    private boolean isDiag(int x1, int y1, int x2, int y2){ return((x2-x1) == (y2-y1) || (x2-x1) == -(y2-y1)); }

    private void printBoard(){
        System.out.println("\n       0     1      2      3      4      5      6      7");
        System.out.println("    --------------------------------------------------------");
        for(int y = 0;y<board.length;y++){
            System.out.print(y + " ");
            for(int x = 0;x<board.length;x++){
                System.out.print(" | ");

                if(board[x][y] == null){
                    System.out.print("    ");
                }else{
                    System.out.print(board[x][y].symbol());
                }
                if((x+1 == board.length)){
                    System.out.print(" | ");
                }
            }
            System.out.println("");
            System.out.println("   ---------------------------------------------------------");
        }
        System.out.println("");
    }

    private Boolean doTurn(Color color){
        boolean[] success;
        boolean successfulMove,ate;
        System.out.println("Player " + color + " - input move:" );
        int pieceX,pieceY,targX,targY;
        String[] pieceCoord,targetCoord;
        while(true){
            try{
                String[] temp = s.nextLine().replaceAll("\\s+","").split("=>");
                pieceCoord = temp[0].split(",");
                targetCoord = temp[1].split(",");

                pieceX = Integer.parseInt(pieceCoord[0]);
                pieceY = Integer.parseInt(pieceCoord[1]);
                targX = Integer.parseInt(targetCoord[0]);
                targY = Integer.parseInt(targetCoord[1]);
                break;
            }catch(Exception e){
                System.out.println("Incorrectly formatted input - " + e);

            }
        }
        System.out.println("");

        Piece selectedPiece = board[pieceX][pieceY];

        if(!spotTaken(pieceX,pieceY)){
            System.out.println("No piece exists at (" + pieceX + "," + pieceY + ")");
            return false;
        }
        if(selectedPiece.getColor() != color){
            System.out.println("That's your opponent's piece.");
            return false;
        }
        if (verbose) {System.out.println("Attempting move of piece: " + Arrays.toString(pieceCoord) + " to position: " + Arrays.toString(targetCoord));}

        success = movePiece(selectedPiece,targX,targY);
        successfulMove = success[0];
        ate = success[1];
        if (verbose) {System.out.println("\nRESULTS:");}
        if (verbose) {System.out.println("Succesful move: " + successfulMove);}
        if (verbose) {System.out.println("Ate:            " + ate);}
        System.out.println("");

        if(successfulMove && ate && canEat(selectedPiece)){
            printBoard();
            System.out.println("You are able to go again! Would you like to go again? [y/n]");
            if(s.nextLine().equals("y")){
                doTurn(color);
            }else{
                System.out.println("You chose not to go again.");
            }
        }
        return success[0];
    }
    private boolean canEat(Piece p){
        int x,y;
        x = p.getX() + 2;
        y = p.getY() + 2;
        if (verbose) {System.out.println("Checking eatability at (" + x + "," + y + "):");}
        if(isValidMove(p,x,y)){
            return true;
        }
        x = p.getX() + 2;
        y = p.getY() - 2;
        if (verbose) {System.out.println("Checking eatability at (" + x + "," + y + "):");}
        if(isValidMove(p,x,y)){
            return true;
        }
        x = p.getX() - 2;
        y = p.getY() - 2;
        if (verbose) {System.out.println("Checking eatability at (" + x + "," + y + "):");}
        if(isValidMove(p,x,y)){
            return true;
        }
        x = p.getX() - 2;
        y = p.getY() + 2;
        if (verbose) {System.out.println("Checking eatability at (" + x + "," + y + "):");}
        if(isValidMove(p,x,y)){
            return true;
        }
        return false;
    }

    private void switchTurn(){
        if(currentTurn == Color.black){
            currentTurn = Color.white;
        }else{
            currentTurn = Color.black;
        }
    }

    private void instruct(){
        System.out.println("Welcome to checkers!");
        System.out.println("Enter your move like this: pieceX,pieceY => newSpotX,newSpotY");
        System.out.println("For example: 1,2 => 2,3");
    }

    private void play(){
        populate();
        instruct();
        while(winner == Color.none){
            printBoard();
            if(doTurn(currentTurn)){
                switchTurn();
            }
            winner = checkWin();

        }
        System.out.println("\uD83C\uDF89 Winner: " + winner + " !!! \uD83C\uDF89");
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Verbose logging? [y/n]");
        boolean v = s.nextLine().equals("y");
        System.out.println("\n\n");
        Game g = new Game(v);
        g.play();

    }
}