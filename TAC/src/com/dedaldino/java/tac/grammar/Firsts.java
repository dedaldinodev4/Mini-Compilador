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
public class Firsts {
    
    public static final String program = Expression.KEYWORDS.get(0);
    public static final List<String> datatypes = Arrays.asList(
        Expression.TIPOS.get(0),Expression.TIPOS.get(1));
    public static final String command =Expression.KEYWORDS.get(2);
    public static final List<String> commands = Arrays.asList(
        Expression.KEYWORDS.get(2),
        Expression.KEYWORDS.get(5),  Expression.KEYWORDS.get(9),
        Expression.KEYWORDS.get(4), Expression.KEYWORDS.get(8)
    );
    public static final List<String> exp = Arrays.asList(
        Expression.TERMINADOR.get(0),
        Expression.OP_ARIT.get(2),Expression.OP_ARIT.get(3),
        Expression.OP_ARIT.get(0),Expression.OP_ARIT.get(1)
    );
    
    public static final List<String> termo = Arrays.asList(
        Expression.TERMINADOR.get(0),
        Expression.OP_ARIT.get(2),
        Expression.OP_ARIT.get(3)
    );
    
    public static final List<String> fator = Arrays.asList(
        Expression.TERMINADOR.get(0));
    
}
