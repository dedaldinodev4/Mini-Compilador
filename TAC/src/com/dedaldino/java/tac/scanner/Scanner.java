/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.scanner;

import com.dedaldino.java.tac.grammar.Expression;
import com.dedaldino.java.tac.utils.Reader;
import com.dedaldino.java.tac.errors.Error;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Scanner {
    
    private String word="";
    private String currentLine;
    private Reader reader;
    private boolean comment = false;
    private BufferedReader fileReader;
    private List<Tokens> tableTokens = new ArrayList();
    private List<Error> tableErrors = new ArrayList();
    
    
    public List<Tokens> getListTokens() {
        return tableTokens;
    }
    public List<Error> getListErrors() {
        return tableErrors;
    }
    
    public void init(String fileName) throws FileNotFoundException {
        this.fileReader = new BufferedReader(new FileReader(fileName));
    }
    
    public void exec() throws IOException {
        //* Variables Initialize *//
        reader = new Reader();
        String currentWord;
        int lengthLine;
        
        while(true) {
            
            reader.nextLine();
            currentLine = fileReader.readLine();
            if (currentLine == null){
                break;
            }
            lengthLine = currentLine.length();
            
            currentLine = currentLine.split("\r")[0];
            currentWord = "";
            
            for (int idx = 0; idx < lengthLine; idx++) {
                currentWord = currentLine.substring(idx, idx+1);
                
                if(Expression.DELIMITADOR.contains(currentWord)) {
                    //* Validation Comment line *//
                    if((!this.comment) && (this.word.length() >= 2) 
                            && (word.substring(0,2).equals("//"))) {
                        word = "";
                        break;
                    }
                    
                    //* Vaidation Multiple Comment *//
                    if((!comment) && (word.length() >= 1) && 
                            (word.substring(0,1).equals("{"))){
                        word = "";
                        comment = true;
                    }
                    if((comment) && (word.length() >= 1) 
                            && (word.substring(0,1).equals("}"))){
                        word = "";
                        comment = false;
                    }
                    
                    if(!comment){
                        if((!word.equals("")) && (!word.contains("}"))){ 
                            this.validateToken(word,reader.getLine());
                            
                        }
                            
                    }
                    word = "";
                } else {
                    word += currentWord;
                }
            }
        }
    }
    
    public void validateToken(String lex, int line) {
        
        if(Expression.isNumero(lex)){
            tableTokens.add(Tokens.addToken("Número",lex,line));
            return;
        }
        if(Expression.isBOOL(lex)){
            tableTokens.add(Tokens.addToken("Boolean",lex, line));
            return;
        }
        if(Expression.isKeywords(lex)){
            tableTokens.add(Tokens.addToken("Palavra Reservada",lex, line));
            return;
        }
        if(Expression.isOperadorLogico(lex)){
            tableTokens.add(Tokens.addToken("Operador Lógico",lex,line));
            return;
        }
        if(Expression.isOperadorRelacional(lex)){
            tableTokens.add(Tokens.addToken("Operador Relacional", lex, line));
            return;
        }
        if(Expression.isOperadorAritmetico(lex)){
            tableTokens.add(Tokens.addToken("Operador Aritmetico", lex, line));
            return;
        }
        if(Expression.isTerminador(lex)){
            tableTokens.add(Tokens.addToken("Delimitador", lex, line));
            return;
        }
        if(!Expression.KEYWORDS.contains(lex)){
            if(Expression.isIdentificador(lex)){
                tableTokens.add(Tokens.addToken("Identificador", lex, line));
                Symbol symbol = new Symbol();
                symbol.setName(lex);
                symbol.addSymbol(symbol);
                return;
            }
        }
        tableErrors.add(Error.addErrorLexical(lex, line));
        
    }
    
    
    
}
