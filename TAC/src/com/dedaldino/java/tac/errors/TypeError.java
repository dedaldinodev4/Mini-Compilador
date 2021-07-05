/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.errors;

/**
 *
 * @author Dedaldino Daniel
 */
public class TypeError {
   
    public static void ErrorSemantico(int error, String token, int line){
        String value;
        
        switch (error) {
            case 0:
                value = "Identificador \""+ token + "\" já foi declarado.";
                break;
            case 1:
                value = "Variável \""+ token + "\" não declarada.";
                break;
            default:
              value = "";
              break;
        }
        
        Error.execError("Semântico", value, line);

    }
    
    public static void ErrorSemantico(int error,String type1, String type2, int line){
        String value;
        
         switch (error) {
            case 2:
                value = "Tipos Incompativeis,  \""+ type1 +"\" para "+type2;
                break;
            case 4:
                 value = "A variável precisa ser do tipo inteiro.";
                break;
            case 5:
                value = "Tipo de Dado da variável precisa ser númerico!";
                break;
            default:
              value = "";
              break;
        }
        
        Error.execError("Semântico", value, line);
    }
    
    public static void ErrorSintatico(String token, String token_validated, int line){
        String value = "Token Esperado: \""+token_validated+"\" em vez de \""+token;
        Error.execError("Sintático", value, line);
    }
    
    public static void ErrorSintatico(int error, String token_unvalidated, int line){
        String value;
        
        switch (error) {
            case 0:
                value = "Esperava encontrar uma variável . não \""+token_unvalidated+"\" na linha "+line;
                break;
            case 1:
                value = "Esperava encontrar um identificador válido. não \""+token_unvalidated+"\" na linha "+line;
                break;
            case 2:
                value = "Esperava encontrar um comando, na linha "+line;
                break;
            case 3:
                value = "Esperva encontrar um número na linha "+line;
                break;
            case 4:
                value = "Tipo de Dado da variável precisa ser númerico!";
                break;
            case 5:
                value = "Esperava encontrar um operador relacional ou lógico, na linha "+line;
                break;
            default:
              value = "";
              break;
        }
        
        Error.execError("Sintático", value, line);
    }
    
    
    
    
    
    
}
