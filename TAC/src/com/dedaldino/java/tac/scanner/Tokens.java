/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.scanner;

/**
 *
 * @author Dedaldino Daniel
 */
public class Tokens {
    
    private String tag;
    private String lex;
    private int line;
    
    
    public String getTag(){
        return tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    } 
    public String getLex(){
        return lex;
    }
    public void setLex(String lex){
        this.lex = lex;
    }
    public int getLine(){ 
        return line;
    }
    public void setLine(int line){ 
        this.line = line;
    }
    
    public static Tokens addToken(String tag, String lex, int line) {
        Tokens token = new Tokens();
        token.setTag(tag);
        token.setLex(lex);
        token.setLine(line);
        return token;
    }
}
