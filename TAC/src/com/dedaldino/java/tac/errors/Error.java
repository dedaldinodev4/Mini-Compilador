/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.errors;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Error {
    
    private String typeError;
    private String description;
    private int lineError;
    private static final List<Error> tableError = new ArrayList();
    
   
    public final void addError(Error error){
        Error.tableError.add(error);
    }
    public static final void cleanError(){
        Error.tableError.clear();
    }
   
    
    public String getTypeError(){ 
        return typeError; 
    }
    public void setTypeError(String typeError){ 
        this.typeError = typeError; 
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public int getLineError(){
        return lineError;
    }
    public void setLineError(int lineError){
        this.lineError = lineError;
    }
    public static List<Error> getErrors(){
        return tableError;
    }
    
    public static Error addErrorLexical(String lex, int line) {
        Error error = new Error();
        error.setTypeError("Lexical");
        error.setDescription(lex);
        error.setLineError(line);
        error.addError(error);
        return error;
    }
    
    public static void execError(String typeError, String description, int line){
        Error error = new Error();
        error.setTypeError(typeError);
        error.setDescription(description);
        error.setLineError(line);
        error.addError(error);
        
        return;
    }
}
