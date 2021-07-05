/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class TableSymbol {
    private List<Symbol> tableSymbol;
    
    public TableSymbol() {
        tableSymbol = new ArrayList();
    }
    
    public int addVariable(String name, DataTypes datatype, int escope){
        if (!existVariable(name)) {
            Symbol symbol = new Symbol(name, datatype, escope);
            tableSymbol.add(symbol);
            return tableSymbol.size() - 1;
        }
        return -1;
    }
    
    public boolean existVariable(String name) {
        for (Symbol table : tableSymbol) {
            if(table.getName().equals(name)) return true;
        }
        return false;
    }
    
    public DataTypes getDataTypeVariable(String name) {
        for (Symbol table: tableSymbol) {
            if (table.getName().equals(name))
                return table.getDataType();
        }
        return null;
    }
    
    public List<Symbol> getTableSymbol() {
        return tableSymbol;
    }
}
