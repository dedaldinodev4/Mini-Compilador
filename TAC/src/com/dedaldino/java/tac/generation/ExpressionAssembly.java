/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.generation;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class ExpressionAssembly {
    
    public static List<String> OP_REL =  Arrays.asList("beq","bne","bltz","bgtz","blez","bgez");   
    public static List<String> OP_LOG = Arrays.asList("and","or");
    public static List<String> OP_ARIT = Arrays.asList("add","sub","mul","div","addi","mul.s","");    
    public static List<String> KEYWORDS  = Arrays.asList(".data",".text",
                                     "main:","syscall","move","mov.s","jal",".globl main");
    public static List<String> DATATYPES = Arrays.asList(".word",".asciiz",".float",".byte");
    public static List<String> SYMBOLS = Arrays.asList("#",";");
        
    public static boolean isKeywords(String lexema){return KEYWORDS.contains(lexema);}
    public static boolean isOperatorArit(String lexema){ return OP_ARIT.contains(lexema);}
    public static boolean isOperatorLog(String lexema){ return OP_LOG.contains(lexema);}
    public static boolean isOperatorRel(String lexema){ return OP_REL.contains(lexema);}
    public static boolean isDatatypes(String lexema){ return DATATYPES.contains(lexema);}
}
