/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.scanner;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Symbol {
    
    private String name;
   private static final List<Symbol> tableSymbol = new ArrayList();
   
   
   public String getName(){
       return name;
   }
   public void setName(String name){
       this.name = name;
   }
    public static List<Symbol> getTableSymbol(){
       return tableSymbol;
   }
   public void addSymbol(Symbol sym){
       if(!Symbol.tableSymbol.contains(sym)){
           Symbol.tableSymbol.add(sym);
       }
   }
   public String toString(){
       return this.getName();
   }
   
   public boolean equals(Object objecto){
       Symbol sym = (Symbol) objecto;
       return sym.getName().equals(this.name);
   }
}
