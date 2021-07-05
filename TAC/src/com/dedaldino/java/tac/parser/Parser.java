/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.parser;

import com.dedaldino.java.tac.errors.TypeError;
import com.dedaldino.java.tac.grammar.Expression;
import com.dedaldino.java.tac.grammar.Firsts;
import com.dedaldino.java.tac.scanner.Tokens;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Parser {
    
    private static List<Tokens> ListTokens;
    private static int globalEscope;
    private static int nextToken;
    private static int currentToken;
    private static TableSymbol tableSymbol;
    
    
    public TableSymbol getTable(){
        return tableSymbol;
    }
    
    public void init(List<Tokens> tokens) {
        globalEscope = 0;
        currentToken = 0;
        tableSymbol = new TableSymbol();
        
        //* Insert in to tokensList *//
        ListTokens = tokens;
    }
    
    public void parserExecute(){
        PROGRAM();
    }
    
    public static int nextTokenFromTable(){
        if (nextToken < ListTokens.size()) {
            return nextToken += 1;
        }else {
            return nextToken;
        }      }
    
    
    public static int getCurrentToken() {
        return currentToken;
    }
    
    public static int getNextToken() {
        return nextToken;
    }
    
    
    
    //* COVIDLG _ TAC *//
    public static void PROGRAM() {
        /** <PROG> ::= program <identificador>; <bloco> **/
        if (ListTokens.get(getNextToken()).getLex().equals(Firsts.program)) {
            nextTokenFromTable();
            if (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex()) && 
                    Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())) {
                tableSymbol.addVariable(ListTokens.get(getNextToken()).getLex(),DataTypes.NULO,globalEscope);
                nextTokenFromTable();
                if (Expression.TERMINADOR.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
                    nextTokenFromTable();
                    BLOCK();
                    if(Expression.KEYWORDS.contains(ListTokens.get(getNextToken()).getLex())
                    && Expression.KEYWORDS.get(3).equals(ListTokens.get(getNextToken()).getLex())) {
                        
                        nextTokenFromTable();
                       
                        if(!Expression.TERMINADOR.contains(ListTokens.get(getNextToken()).getLex())
                            && !Expression.TERMINADOR.get(7).equals(ListTokens.get(getNextToken()).getLex())){
                            TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                                    Expression.TERMINADOR.get(7), ListTokens.get(getNextToken()).getLine());
                        return;
                        }
                    }
                    else {
                        
                        
                        TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                                    Expression.KEYWORDS.get(3), ListTokens.get(getNextToken()).getLine());
                        return;
                    } 
                } else {
                    
                    TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                                    Expression.TERMINADOR.get(6), ListTokens.get(getNextToken()).getLine());
                        return;
                }
            } else {
               
                TypeError.ErrorSintatico(0,ListTokens.get(getNextToken()).getLex(), 
                        ListTokens.get(getNextToken()).getLine());
                return;
            }

        } else {
           
            
            TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                Firsts.program, ListTokens.get(getNextToken()).getLine());
            return;
        }

    }
    
    public static void BLOCK() {
       /**
         * <bloco>::= [<parte de declarações de variavéis>]
         *            <comando composto>
        */
       VAR();
       BEGIN();
       
    }
    
    public static void LIST_IDENT(String var, DataTypes datatype) {
        if (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                && Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())){
            if(tableSymbol.addVariable(var, datatype, globalEscope) < 0){
                TypeError.ErrorSemantico(0,var , ListTokens.get(getNextToken()).getLine());
                return;
            }
            
            nextTokenFromTable();
        
            if (Expression.TERMINADOR.get(6).equals(ListTokens.get(getNextToken()).getLex())) {
                 nextTokenFromTable();
                LIST_IDENT(ListTokens.get(getNextToken()).getLex(), datatype);
            }
        }
    }
    
    public static void VALIDATE_IDENTS() {
        if (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                && Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())){
            
            if(!tableSymbol.existVariable(ListTokens.get(getNextToken()).getLex())){
                TypeError.ErrorSemantico(1, ListTokens.get(getNextToken()).getLex(), 
                        ListTokens.get(getNextToken()).getLine());
                return;
            }
            
            nextTokenFromTable();
            
            if (Expression.TERMINADOR.get(6).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                VALIDATE_IDENTS();
            }

        } else {
            TypeError.ErrorSintatico(0,ListTokens.get(getNextToken()).getLex(), 
               ListTokens.get(getNextToken()).getLine());
            return;
        }   
    }
    
    public static void VAR() {
        if (Firsts.datatypes.contains(ListTokens.get(getNextToken()).getLex())) {
            DataTypes datatype = getType(ListTokens.get(getNextToken()).getLex());
            nextTokenFromTable();
            String var = ListTokens.get(getNextToken()).getLex();
            LIST_IDENT(var, datatype);
            
            if (Expression.TERMINADOR.contains(ListTokens.get(getNextToken()).getLex())
                    && Expression.TERMINADOR.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                VAR();
            } else {
                 TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                                Expression.TERMINADOR.get(4), ListTokens.get(getNextToken()).getLine());
                return;
            }
        }
    }
    
    public static void CMD() {
        if (Firsts.commands.contains(ListTokens.get(getNextToken()).getLex())
                || (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex()) && 
                Expression.isIdentificador(ListTokens.get(getNextToken()).getLex()))) {
            CMD2();
            if (Expression.TERMINADOR.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                CMD();
            }
        }
    }
    
    public static void CMD2() {
        
        if (Firsts.commands.contains(ListTokens.get(getNextToken()).getLex())
                || Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())) {
            
            /** <comando> := <comando composto> **/
            if (Firsts.commands.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
                BEGIN();
            }
            
            /** <comando> := <read> **/
            else if (Firsts.commands.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
                READER();
            }
            /** <comando> := <write> **/
            else if (Firsts.commands.get(3).equals(ListTokens.get(getNextToken()).getLex())) {
                WRITER();
            }
            /** <comando> := <comando condicional 1> **/
            else if (Firsts.commands.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
                CONDITIONAL();
            }
            /** <atribuicao> := <variavel> := <expressao> **/
            else if (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                    && Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())) {
                
                String var = ListTokens.get(getNextToken()).getLex();
                
                if(!tableSymbol.existVariable(var)){
                    TypeError.ErrorSemantico(1, var, ListTokens.get(getNextToken()).getLine());
                    return;
                }
                DataTypes type_var = tableSymbol.getDataTypeVariable(var);  
                nextTokenFromTable();
                
                if (Expression.OP_ARIT.get(5).equals(ListTokens.get(getNextToken()).getLex())) {
                    nextTokenFromTable();
                    
                    DataTypes type_expr = expr();
                    if(type_var == DataTypes.INTEIRO && type_expr == DataTypes.BOOLEAN){
                        TypeError.ErrorSemantico(2, type_var.toString(), 
                            type_expr.toString(),
                            ListTokens.get(getNextToken()).getLine());
                        return;
                    }else if(type_var == DataTypes.BOOLEAN && type_expr != DataTypes.BOOLEAN){
                        TypeError.ErrorSemantico(2, type_var.toString(), 
                            type_expr.toString(),
                            ListTokens.get(getNextToken()).getLine());
                        return;
                    }else if(type_var == DataTypes.INTEIRO && type_expr != DataTypes.INTEIRO ){
                        TypeError.ErrorSemantico(2, type_var.toString(), 
                            type_expr.toString(),
                            ListTokens.get(getNextToken()).getLine());
                        return;
                    }
                        
                } else {
                    TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                        Expression.OP_ARIT.get(4), 
                        ListTokens.get(getNextToken()).getLine());
                    return;
                }
                
            } else {
                TypeError.ErrorSintatico(2,ListTokens.get(getNextToken()).getLex()
                        ,ListTokens.get(getNextToken()).getLine());
                return;
            }

        }
    }
    
    public static void BEGIN() {
        if (Firsts.commands.get(0).equals(ListTokens.get(getNextToken()).getLex())){       
            nextTokenFromTable();
            CMD();
            if(Expression.isKeywords(ListTokens.get(getNextToken()).getLex()) &&
                Expression.KEYWORDS.get(3).equals(ListTokens.get(getNextToken()).getLex())){
                nextTokenFromTable();
               
            } else {
                TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.KEYWORDS.get(3), 
                    ListTokens.get(getNextToken()).getLine());
                return;
            }
        } else {
            TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.KEYWORDS.get(2), 
                    ListTokens.get(getNextToken()).getLine());
                return;
        }
    }
    
    public static void CONDITIONAL() {
     
        /** <comando> ::= if <bool> then <cmd> [else <else>] **/
        if (Firsts.commands.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            if(Expression.TERMINADOR.get(0).equals(ListTokens.get(getNextToken()).getLex())){
                nextTokenFromTable();
                BOOL();
                
                if(Expression.TERMINADOR.get(1).equals(ListTokens.get(getNextToken()).getLex())){
                    nextTokenFromTable();
                    if (Expression.KEYWORDS.get(10).equals(ListTokens.get(getNextToken()).getLex())) {
                        nextTokenFromTable();
 
                        CMD();
                        ELSE();

                    } else {
                        TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                            Expression.KEYWORDS.get(10), 
                            ListTokens.get(getNextToken()).getLine());
                        return;
                    }
                } else {
             
                    TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                        Expression.TERMINADOR.get(1), 
                        ListTokens.get(getNextToken()).getLine());
                    return;
                }
                
                 
            } else {
                TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.TERMINADOR.get(0), 
                    ListTokens.get(getNextToken()).getLine());
                return;
            }      
        }
    }
    
    public static void ELSE() {
        if(Expression.KEYWORDS.get(6).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            CMD();
        }
    }
    
    public static void READER() {
        if (Firsts.commands.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            if (Expression.TERMINADOR.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                VALIDATE_IDENTS();
                if (Expression.TERMINADOR.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
                    nextTokenFromTable();
                } else {
                    TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                        Expression.TERMINADOR.get(1), 
                        ListTokens.get(getNextToken()).getLine());
                    return;
                }
            } else {
                TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.TERMINADOR.get(0), 
                    ListTokens.get(getNextToken()).getLine());
                return;
            }
        }
    }
    
    public static void WRITER() {
        
        if (Firsts.commands.get(3).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            if (Expression.TERMINADOR.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                LIST_ARG();
                if (Expression.TERMINADOR.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
                    nextTokenFromTable();
                } else {
                    TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                      Expression.TERMINADOR.get(1), 
                      ListTokens.get(getNextToken()).getLine());
                    return;
                }
            } else {
                TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.TERMINADOR.get(0), 
                   ListTokens.get(getNextToken()).getLine());
                return;
            }
        }
    }
    
    public static void LIST_ARG() {
        expr();
        LIST_ARG2();
    }
    
    public static void LIST_ARG2() {
        if(Expression.TERMINADOR.get(6).equals(ListTokens.get(getNextToken()).getLex())){
            nextTokenFromTable();
            LIST_ARG();
        }
    }
    
    
    public static DataTypes expr() {
        /** <expr> ::= <term> <expr2> **/
        
        DataTypes type_term = term();
        DataTypes type_expr2 = expr2();
        
        if(type_term == DataTypes.INTEIRO || 
                type_expr2 == DataTypes.INTEIRO){
            return DataTypes.INTEIRO;
        }else if(type_term == DataTypes.BOOLEAN || 
            type_expr2 == DataTypes.BOOLEAN){
            return DataTypes.BOOLEAN;
        }
        return null;
        
    }

    public static DataTypes expr2() {
        /** <expr2> ::= + <term> <expr2> **/
        /** <expr2> ::= - <term> <expr2> | $ **/
        if (Expression.OP_ARIT.get(0).equals(ListTokens.get(getNextToken()).getLex())||
                Expression.OP_ARIT.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
            
            nextTokenFromTable();
            DataTypes type_term = term();
            DataTypes type_expr2 = expr2();
            
            if(type_term == DataTypes.INTEIRO || 
                type_expr2 == DataTypes.INTEIRO){
                return DataTypes.INTEIRO;
            }else if(type_term == DataTypes.BOOLEAN || 
                type_expr2 == DataTypes.BOOLEAN){
                return DataTypes.BOOLEAN;
            }  
        }
        return null;
    }

    public static DataTypes term() {
        /** <term>::= <fat> <term2> **/
        
        DataTypes type_fat = fat();
        DataTypes type_term = term2();
        
        if(type_term == DataTypes.INTEIRO || 
                type_fat == DataTypes.INTEIRO){
            return DataTypes.INTEIRO;
        }else if(type_term == DataTypes.BOOLEAN || 
                type_fat == DataTypes.BOOLEAN){
            return DataTypes.BOOLEAN;
        }
        
        return null;      
    }

    public static DataTypes term2() {
        /** <term2>::= * <fat> <term2> *
         * <term2>::= div <fat> <term2>
         * <term2>::= and <fat> <term2> | #
        */
        
        if (Expression.OP_ARIT.get(2).equals(ListTokens.get(getNextToken()).getLex()) ||
                Expression.KEYWORDS.get(15).equals(ListTokens.get(getNextToken()).getLex())
            ) {
            
            nextTokenFromTable();            
            
            DataTypes type_fat = fat();
            DataTypes type_term = term2();
            
            if (type_term == DataTypes.INTEIRO
                    || type_fat == DataTypes.INTEIRO) {
                return DataTypes.INTEIRO;
            } else if (type_term == DataTypes.BOOLEAN
                    || type_fat == DataTypes.BOOLEAN) {
                return DataTypes.BOOLEAN;
            } else { 
                return null; 
            }
        } else { 
            return null; 
        }
   
    }

    public static DataTypes fat() {
        /** <fat>::= ( <expr> ) **/
        if (Firsts.fator.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            
            DataTypes type_expr = BOOL();
            if (Expression.TERMINADOR.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
                nextTokenFromTable();
                return type_expr;
            } else {
                 TypeError.ErrorSintatico(ListTokens.get(getNextToken()).getLex(),
                    Expression.TERMINADOR.get(1), 
                    ListTokens.get(getNextToken()).getLine());
                
            }
        } /** <fat> ::= <var> **/
        else if (!Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                && Expression.isIdentificador(ListTokens.get(getNextToken()).getLex())) {
            String var = ListTokens.get(getNextToken()).getLex();
            if(!tableSymbol.existVariable(var)){
                TypeError.ErrorSemantico(1, var,ListTokens.get(getNextToken()).getLine());
            }
            DataTypes typeVar = tableSymbol.getDataTypeVariable(var);
            nextTokenFromTable();
            return typeVar;
        }/** <fat> ::=  <num> **/
        else if (Expression.isInteiro(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            return DataTypes.INTEIRO;
        }/** <fat> ::=  <bool> **/
        else if (Expression.isBOOL(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            return DataTypes.BOOLEAN;
        }
        /** <fat> ::= not <fat> **/
        else if(Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                && Expression.KEYWORDS.get(14).equals(ListTokens.get(getNextToken()).getLex())){
            nextTokenFromTable();
            fat();
        }
        return null;
        
    }

    public static DataTypes BOOL() {
        /** <bool>::= <expr> <bool> **/
        if ((!Expression.isKeywords(ListTokens.get(getNextToken()).getLex())
                && Expression.isIdentificador(ListTokens.get(getNextToken()).getLex()))
                || Expression.isNumero(ListTokens.get(getNextToken()).getLex())
                || Expression.TERMINADOR.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
            DataTypes expr_type = expr();
            BOOL2();
            return expr_type;
        }
        return null;
    }

    public static DataTypes BOOL2() {
        /** <bool2> ::= <relacao> <expr> **/
        if (Expression.isOperadorLogico(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
            DataTypes expr2_type = expr();
            return expr2_type;
        }
        
        else if ( 1 == 1)
        {
            op_real();
            DataTypes expr_type = expr();
            return expr_type;
        } 
        
        return null;
    }

    public static void op_real() {
        if (Expression.OP_ARIT.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
        } 
        else if (Expression.OP_REL.get(0).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
        } 
        else if (Expression.OP_REL.get(1).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable(); 
        }
        else if (Expression.OP_REL.get(4).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();      
        }
        else if (Expression.OP_REL.get(2).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
           
        } 
        else if (Expression.OP_REL.get(3).equals(ListTokens.get(getNextToken()).getLex())) {
            nextTokenFromTable();
        } else {
            TypeError.ErrorSintatico(5,
                  ListTokens.get(getNextToken()).getLex(), 
                   ListTokens.get(getNextToken()).getLine());
            return;
        }

    }    
    
    private static DataTypes getType(String datatype){
       if (Firsts.datatypes.contains(datatype)) {
            if (datatype.equals(Firsts.datatypes.get(0))) {
                return DataTypes.INTEIRO;
            } else if (datatype.equals(Firsts.datatypes.get(1))) {
                return DataTypes.BOOLEAN;
            }
        }
        return null; 
    } 
    
}
