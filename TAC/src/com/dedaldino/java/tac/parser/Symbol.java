/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.parser;

/**
 *
 * @author Dedaldino Daniel
 */
public class Symbol {
    
    private String name;
    private DataTypes datatype;
    private int escope;
     
    
    public Symbol(String name, DataTypes datatype, int escope){
        this.name = name;
        this.datatype = datatype;
        this.escope = escope;
        
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public DataTypes getDataType() {
        return datatype;
    }
    public void setDataType(DataTypes datatype) {
        this.datatype = datatype;
    }
    
    public int getEscope() {
        return escope;
    }
    public void setEscope(int escope) {
        this.escope = escope;
    }
    
    
    
    
}
