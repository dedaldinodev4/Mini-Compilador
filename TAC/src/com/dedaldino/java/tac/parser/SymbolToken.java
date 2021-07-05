/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.parser;

import com.dedaldino.java.tac.scanner.Tokens;

/**
 *
 * @author Dedaldino Daniel
 */
public class SymbolToken {
    private String lex;
    private int datatype;
    private int escope;
    
    
    public SymbolToken(Tokens token, int datatype, int escope){
        this.lex = token.getLex();
        this.datatype = datatype;
        this.escope = escope;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public int getDatatype() {
        return datatype;
    }

    public void setDatatype(int datatype) {
        this.datatype = datatype;
    }

    public int getEscope() {
        return escope;
    }

    public void setEscope(int escope) {
        this.escope = escope;
    }
    
    
}
