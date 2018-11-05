package com.company;


public class Piece {
    private Color color;
    private Boolean isKing;
    private int x;
    private int y;
    private Boolean canEat;

    Piece(Color color, int x, int y){
        this.color = color;
        this.x = x;
        this.y = y;
        this.isKing = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean isKing() {
        return isKing;
    }

    public void setKing(Boolean king) {
        isKing = king;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y){
        setX(x);
        setY(y);
    }

    public String symbol(){
        if(getColor() == Color.black){
            if(isKing()){
                return "[BK]";
            }else{
                return "[BN]";
            }
        }else{
            if(isKing()){
                return "[WK]";
            }else{
                return "[WN]";
            }
        }
    }


    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", isKing=" + isKing +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Piece c = new Piece(Color.black,1,1);
        System.out.println(c);
    }
}
