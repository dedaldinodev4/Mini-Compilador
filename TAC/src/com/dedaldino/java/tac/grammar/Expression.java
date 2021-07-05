/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.grammar;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Expression {
        //* Expressões Regulares  *//
        public static String NUMERO = "^\\d+|^\\d+\\.?\\d+";
        public static String IDENT  = ("[a-zA-Z_][a-zA-Z0-9_]*");
        public static String INTEIRO = ("^\\d+");
        public static List<String> BOOL = Arrays.asList("true", "false");
        public static List<String> SIMB = Arrays.asList("\"", "%","!");
        public static List<String> DELIMITADOR = Arrays.asList(" ","\\n","");
        public static List<String> OP_REL =  Arrays.asList("<",">", ">=","<=","<>","=");
        public static List<String> OP_LOG = Arrays.asList("and","or");
        public static List<String> OP_ARIT = Arrays.asList("+","-","*","/","=",":=");
        
        public static List<String> KEYWORDS  = Arrays.asList("program",
                                     "procedure","begin","end"
                                    ,"write","if","else","for","read",
                                    "while","then","var","int",
                                    "boolean","not","div","false", "true", "do");
        public static List<String> TERMINADOR = Arrays.asList("(",")","[","]",";",":",",",".");
        public static List<String> TIPOS = Arrays.asList("int","boolean");
        
       
       
        public static boolean isNumero(String lexema){ return lexema.matches(NUMERO); }
        public static boolean isIdentificador(String lexema){ return lexema.matches(IDENT);}
        public static boolean isBOOL(String lexema){ return BOOL.contains(lexema);}
        public static boolean isInteiro(String lexema){ return lexema.matches(INTEIRO);}
        public static boolean isKeywords(String lexema){return KEYWORDS.contains(lexema);}
        public static boolean isTerminador(String lexema){ return TERMINADOR.contains(lexema);}
        public static boolean isOperadorAritmetico(String lexema){ return OP_ARIT.contains(lexema);}
        public static boolean isOperadorLogico(String lexema){ return OP_LOG.contains(lexema);}
        public static boolean isOperadorRelacional(String lexema){ return OP_REL.contains(lexema);}
}
