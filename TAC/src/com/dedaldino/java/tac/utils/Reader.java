/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.utils;

/**
 *
 * @author Dedaldino Daniel
 */
public class Reader {
    
    private int line;
    private int column;
    
    public Reader(){
        line = 0;
        column = 0;
    }
    
    public int getLine(){
        return line;
    }
    public int getColumn(){
        return column;
    }
    public void setLine(int line){
        this.line = line;
    }
    public void setColumn(int column){
        this.column = column;
    }
    public void nextLine(){
        this.line++;
        this.column = 0;
    }
    public void nextColumn(){
        column++;
    }
    public void nextColumn(int value){
        column += value;
    }
    public String toString(){
        return "\t"+ line +"\t "+ column;
    }
}
