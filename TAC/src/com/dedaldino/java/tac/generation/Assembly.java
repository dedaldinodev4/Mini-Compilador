/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.generation;

import com.dedaldino.java.tac.grammar.Expression;
import com.dedaldino.java.tac.scanner.Tokens;
import com.dedaldino.java.tac.parser.DataTypes;
import com.dedaldino.java.tac.parser.TableSymbol;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dedaldino Daniel
 */
public class Assembly {
    private static List<String> commands = new ArrayList<String>();
    private static List<String> variables = new ArrayList<String>();
    private static List<String> dataTypes = new ArrayList<String>();
    private static int nextToken = 0;
    private static List<Tokens> listTokens;
    private static TableSymbol tableSymbol;
    private static BufferedWriter file;
    private static int counter1 = 0, counter2 = 0, counter3 = 0;
    
    
    public void init(List<Tokens> listTokens, TableSymbol tableSymbol) {
        this.listTokens = listTokens;
        this.tableSymbol = tableSymbol;
    }
    
    public void execute() throws IOException {
        PROGRAM();
    } 
    
    public static int nextTokenAssembly() {
        if (nextToken < listTokens.size()) {
            return nextToken += 1;
        }else {
            return nextToken;
        }      
    }

    public static int getNextToken(){
        return nextToken;
    }
    
    public static void writter(String command) throws IOException {
        file.write(command);
        file.newLine();
    }
    
    public static void PROGRAM() throws IOException{
        FileWriter fileName = new FileWriter("source.asm");
        file = new BufferedWriter(fileName);
        
        if(Expression.KEYWORDS.get(0).equals(listTokens.get(getNextToken()).getLex())) {
            writter(ExpressionAssembly.KEYWORDS.get(0));
            nextTokenAssembly();
            writter(ExpressionAssembly.SYMBOLS.get(0) +
                listTokens.get(getNextToken()).getLex()
            );
            nextTokenAssembly();
            if(Expression.TERMINADOR.get(4).equals(listTokens.get(getNextToken()).getLex())){

                VAR();
            }
            
            writter(ExpressionAssembly.KEYWORDS.get(1));
            BLOCK();
            file.close();
        }
    }
    
    public static void LIST_IDENT(String dataType, String var) throws IOException {
        if(!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
            if(tableSymbol.existVariable(var)) {
                writter(listTokens.get(getNextToken()).getLex() + 
                        Expression.TERMINADOR.get(5) + dataType + " 0 ");
                nextTokenAssembly();
                if(Expression.TERMINADOR.get(6).equals(listTokens.get(getNextToken()).getLex())){
                    nextTokenAssembly();
                    LIST_IDENT(dataType, listTokens.get(getNextToken()).getLex());
                }
            }
        }
    }
    
    public static void VAR() throws IOException{
        nextTokenAssembly();
        if(Expression.TIPOS.contains(listTokens.get(getNextToken()).getLex())){
            if (Expression.TIPOS.get(0).equals(listTokens.get(getNextToken()).getLex())) {
                nextTokenAssembly();
                LIST_IDENT(ExpressionAssembly.DATATYPES.get(0),listTokens.get(getNextToken()).getLex());
                if (Expression.TERMINADOR.get(4).equals(listTokens.get(getNextToken()).getLex())) {
                   VAR();  
                }
            }
            else if(Expression.TIPOS.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                nextTokenAssembly();
                LIST_IDENT(ExpressionAssembly.DATATYPES.get(3),listTokens.get(getNextToken()).getLex());
                if (Expression.TERMINADOR.get(4).equals(listTokens.get(getNextToken()).getLex())) {
                   VAR();  
                }
            }
        } 
        
    }
    
    public static void BLOCK() throws IOException{
        
        if (Expression.KEYWORDS.get(2).equals(listTokens.get(getNextToken()).getLex())) {
           writter(ExpressionAssembly.KEYWORDS.get(7));
            writter(ExpressionAssembly.KEYWORDS.get(2));
            
            for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {

                if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.INTEIRO) {
                    writter("lw $t" + idx + "," + tableSymbol.getTableSymbol().get(idx).getName());
                } else if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.BOOLEAN) {
                    writter("lb $t" + idx + "," + tableSymbol.getTableSymbol().get(idx).getName());
                } 
            }
        }
        nextTokenAssembly();
        CMD();
    }
    
    public static void CMD() throws IOException{
        if (listTokens.get(getNextToken()).getLex().equals(
                Expression.KEYWORDS.get(4))) {
            nextTokenAssembly();
            WRITER();

        } else if (listTokens.get(getNextToken()).getLex().equals(
                Expression.KEYWORDS.get(8))) {
            nextTokenAssembly();
            READER();
        } else if (!Expression.isKeywords(
                listTokens.get(getNextToken()).getLex()) 
            && Expression.isIdentificador(
               listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();
           if(listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(5))){
                nextTokenAssembly();
                EXPR();
            }
        }else if (listTokens.get(getNextToken()).getLex().equals(
            Expression.KEYWORDS.get(5))) {
            nextTokenAssembly();
            CONDITIONAL();
        } else if (listTokens.get(getNextToken()).getLex().equals(
                Expression.TERMINADOR.get(4))) {
            nextTokenAssembly();
            CMD();
        }
    }
    
    public static void CONDITIONAL() throws IOException{
        int reg = 0, reg2 = 0;
        counter2++;
        if (Expression.TERMINADOR.get(0).equals(listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();   
            
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.
                        get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.
                        getTableSymbol().get(idx).getName())) {
                        reg = idx;
                    }
                }
                nextTokenAssembly();
                
                if (listTokens.get(getNextToken()).getLex().equals(
                        Expression.OP_REL.get(5))) {
                    nextTokenAssembly();
                    
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.
                                 get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + reg + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                    Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + reg + ",$t"+ (tableSymbol.getTableSymbol().size() + 1) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }
                    
                }
                
                
                    else if (listTokens.get(getNextToken()).getLex().equals(
                            Expression.OP_REL.get(4))) {
                        nextTokenAssembly();
                        if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                                && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0)+ " $t" + reg + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0) + " $t" + reg + ",$t" + (tableSymbol.getTableSymbol().size() + 1) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                    listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }
                }
                
                else if (listTokens.get(getNextToken()).getLex().equals(
                        Expression.OP_REL.get(1))
                        || listTokens.get(getNextToken()).getLex().equals(
                            Expression.OP_REL.get(2))) {
                   nextTokenAssembly();
 
                   
                    if(!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $t"+(tableSymbol.getTableSymbol().size()+3) + ", $t"+reg + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $t" + (tableSymbol.getTableSymbol().size()+3)  + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " , $t"+(tableSymbol.getTableSymbol().size() + 1) + ", $t"+reg);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $t" + (tableSymbol.getTableSymbol().size() + 2) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                   nextTokenAssembly();
                                   CMD();
                                   writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        } 
                }   
            } 
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(0))
                        || listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(3))) {
                   nextTokenAssembly();
                   
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $t"+(tableSymbol.getTableSymbol().size()+3) + ", $t"+reg + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $t" + (tableSymbol.getTableSymbol().size()+3)  + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " , $t"+(tableSymbol.getTableSymbol().size() + 1) + ", $t"+reg);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $t" + (tableSymbol.getTableSymbol().size() + 2) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        } 
                }
            }
        }
            
        
        else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
               
                writter("li $t" + (tableSymbol.getTableSymbol().size() + 2) + ", " + listTokens.get(getNextToken()).getLex());
                nextTokenAssembly();
                if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(5)) 
                    ) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
                } 
                
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(4)) 
                ) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
                }
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(0))
                        || listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(3))) {
                   nextTokenAssembly();
                   
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $s"+counter2 + ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $s" + counter2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                }
            
            } else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(1))
                    || listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(2))) {
                   nextTokenAssembly();
                   
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $s"+counter2 + ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $s" + counter2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }   
            }
        }
      }
        CMD();
    } 
    
    public static void WRITER() throws IOException {
        if (listTokens.get(getNextToken()).getLex().equals(
                Expression.TERMINADOR.get(0))) {
            nextTokenAssembly();
            LIST_ARG();
            if (listTokens.get(getNextToken()).getLex().equals(
                    Expression.TERMINADOR.get(1))) {
                nextTokenAssembly();
            }
        }
        CMD();
    }
    
    public static void READER() throws IOException{
        if (Expression.TERMINADOR.get(0).equals(listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();
            LIST_VARS();
            if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                nextTokenAssembly();
                CMD();
            }
        }
    }
    
    public static void BOOL() throws IOException {
        int reg = 0, reg2 = 0;
        counter2++;
        if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg = idx;
                    }
                }
                nextTokenAssembly(); 
                //* d == k || d == 2 *//
                if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(5)) 
                ) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + reg + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + reg + ",$t" + (tableSymbol.getTableSymbol().size() + 1) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(Expression.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
                } 
                //* !=
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(4))) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.
                                    getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0) + " $t" + reg + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.
                                get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.
                                    get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + 
                                listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0) + " $t" + reg + ",$t" + 
                                (tableSymbol.getTableSymbol().size() + 1) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.
                                    get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }
                }
                //* d < f || d < 3 *// 
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(0))
                        || listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(3))) {
                   nextTokenAssembly();
                   
                    if(!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.
                                    getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $t"+(tableSymbol.getTableSymbol().size()+3) + ", $t"+reg + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $t" + (tableSymbol.getTableSymbol().size()+3)  + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " , $t"+(tableSymbol.getTableSymbol().size() + 1) + ", $t"+reg);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $t" + (tableSymbol.getTableSymbol().size() + 2) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        } 
                }   
            } 
            //* d > f || d > 4 *//
            else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(1))
                    || listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(2))) {
                   nextTokenAssembly();
                   
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $t"+(tableSymbol.getTableSymbol().size()+3) + ", $t"+reg + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $t" + (tableSymbol.getTableSymbol().size()+3)  + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                        writter("li $t" + (tableSymbol.getTableSymbol().size() + 1) + ", " + 
                                listTokens.get(getNextToken()).getLex());
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " , $t"+(tableSymbol.getTableSymbol().size() + 1) + ", $t"+reg);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $t" + (tableSymbol.getTableSymbol().size() + 2) + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        } 
                }
            }
            }else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
               
                writter("li $t" + (tableSymbol.getTableSymbol().size() + 2) + ", " + 
                        listTokens.get(getNextToken()).getLex());
                nextTokenAssembly(); // 1 == d 
                if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(5)) 
                ) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol()
                                    .get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(1) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " ,$t" + 
                                reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
                } //* 2 != f *// 
                else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_REL.get(4))) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        writter("if:");
                        writter(ExpressionAssembly.OP_REL.get(0) + " $t" + (tableSymbol.getTableSymbol().size()+2) + " ,$t" + reg2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                            listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
                } //* 1 < d *//
                else if (listTokens.get(getNextToken()).getLex().equals(
                        Expression.OP_REL.get(0))
                        || listTokens.get(getNextToken()).getLex().equals(
                            Expression.OP_REL.get(3))) {
                   nextTokenAssembly();
                   
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $s"+counter2+ ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(3) + " $s" + counter2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                        listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                    listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    } 
            } 
                //* 1 > d *//
                else if (listTokens.get(getNextToken()).getLex().equals(
                        Expression.OP_REL.get(1))
                        || listTokens.get(getNextToken()).getLex().equals(
                            Expression.OP_REL.get(2))) {
                   nextTokenAssembly();
                  
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex())
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $s"+counter2+ ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_REL.get(2) + " $s" + counter2+ ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                    listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }   
            }
        }
        
        else if(Expression.isOperadorLogico(
                listTokens.get(getNextToken()).getLex())){
          
          if(Expression.OP_LOG.get(0).equals(listTokens.get(getNextToken()).getLex())){
            nextTokenAssembly();
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex())
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(0) + " $s"+counter2+ ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_LOG.get(0) + " $s" + counter2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(
                                listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                    listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }
          }else if(Expression.OP_LOG.get(1).equals(listTokens.get(getNextToken()).getLex())){
              nextTokenAssembly();
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(
                                    tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        
                        writter("if:");
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $s"+counter2+ ", $t"+(tableSymbol.getTableSymbol().size()+2) + ", $t"+reg2);
                        writter(ExpressionAssembly.OP_LOG.get(1) + " $s" + counter2 + ",falso" + counter2);
                        nextTokenAssembly();
                        if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())) {
                            nextTokenAssembly();
                            if (Expression.KEYWORDS.get(10).equals(
                                    listTokens.get(getNextToken()).getLex())) {
                                nextTokenAssembly();
                                CMD();
                                writter("falso" + counter2 + ":");
                                nextTokenAssembly();
                                if (listTokens.get(getNextToken()).getLex().equals(
                                        Expression.KEYWORDS.get(6))) {
                                    nextTokenAssembly();
                                    CMD();
                                    writter(ExpressionAssembly.KEYWORDS.get(3));
                                }
                            }
                        }
                    }
          }
          
      }
    }
    
    public static void LIST_VARS() throws IOException{
        int reg = 0, t = 0, t1 = 0;
        counter1++;
        if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
            for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                    reg = idx;
                    if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.INTEIRO ) {
                        t++;
                    } else if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.BOOLEAN) {
                        t1++;
                    }
                }
            }

            if (t > 0) {
                writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(8) + counter1);
                writter("li $v0,10");
                writter(ExpressionAssembly.KEYWORDS.get(3));
            } else if (t1 > 0) {
                writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(8) + counter1);
                writter("li $v0,10");
                writter(ExpressionAssembly.KEYWORDS.get(3));
            }

            if (t > 0) {
                writter(Expression.KEYWORDS.get(8) + counter1 + ":");
                writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 5");
                writter(ExpressionAssembly.KEYWORDS.get(3));
                writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + reg + ",$zero, $v0");

            } else if (t1 > 0) {
                writter(Expression.KEYWORDS.get(8) + counter1 + ":");
                writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 8");
                writter(ExpressionAssembly.KEYWORDS.get(3));
                writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + reg + ",$zero, $a0");
            } 

            nextTokenAssembly();
            if (Expression.TERMINADOR.get(6).equals(listTokens.get(getNextToken()).getLex())) {
                nextTokenAssembly();
                LIST_VARS();
            }
        }
    }
    
    public static void LIST_ARG() throws IOException {
        int reg = 0, t = 0, t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5=0;
        counter3++;
        EXPR();
        
        
         nextToken--; 
         if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
             nextToken += 2;
            
             if (listTokens.get(getNextToken()).getLex().equals(Expression.TERMINADOR.get(4))) {
                nextToken -=2;
             
                t4++;
                writter(ExpressionAssembly.OP_ARIT.get(4) + " $a0, $zero" + "," + listTokens.get(getNextToken()).getLex());
                writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(4) + counter3);
                writter("li $v0,10");
                writter(ExpressionAssembly.KEYWORDS.get(3));
             }
         }
         else if(Expression.OP_ARIT.contains(listTokens.get(getNextToken()-2).getLex()))
        {   nextTokenAssembly();
            t5++;
            writter(ExpressionAssembly.OP_ARIT.get(0) + " $a0, $zero" + ",$s"+tableSymbol.getTableSymbol().size());
            writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(4) + counter3);
            writter("li $v0,10");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        }
             
         
        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                reg = idx;
                if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.INTEIRO) {
                    t++;
                } else if (tableSymbol.getTableSymbol().get(idx).getDataType() == DataTypes.BOOLEAN) {
                    t1++;
                }

            }
        }

        if (t > 0) {
            writter(ExpressionAssembly.OP_ARIT.get(0) + " $a0" + ", $zero, $t" + reg);
            writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(4) + counter3);
            writter("li $v0,10");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        
        } else if (t1 > 0) {
            writter("la $a0" + ", " + listTokens.get(getNextToken()).getLex());
            writter(ExpressionAssembly.KEYWORDS.get(6) + " " + Expression.KEYWORDS.get(4) + counter3);
            writter("li $v0,10");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        } 
        
        if (t > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 1");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        } else if (t1 > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 4");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        } else if (t2 > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 2");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        } else if (t3 > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 4");
            writter(ExpressionAssembly.KEYWORDS.get(3));
            
        } else if (t4 > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(Expression.KEYWORDS.get(4) + " $v0, $zero, 1");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        }else if (t5 > 0) {
            writter(Expression.KEYWORDS.get(4) + counter3 + ":");
            writter(ExpressionAssembly.OP_ARIT.get(4) + " $v0, $zero, 1");
            writter(ExpressionAssembly.KEYWORDS.get(3));
        }

        nextTokenAssembly();
        if (listTokens.get(getNextToken()).getLex().equals(Expression.TERMINADOR.get(6))) {
            nextTokenAssembly();
            LIST_ARG();
        }
    }
    
    public static void EXPR() throws IOException{
      int reg = 0, reg1 = 0, rr = 0, reg2 = 0;

        TERM();
                //d=2 || d = j
        if (Expression.TERMINADOR.get(4).equals(listTokens.get(getNextToken()).getLex())) {
            nextToken--;
            rr = nextToken - 2;
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg = idx;
                    }

                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + reg2 + ", $zero" + ", $t" + reg);
                nextTokenAssembly();
                CMD();
            } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                writter(ExpressionAssembly.OP_ARIT.get(4) + " $t" + reg2 + ", $zero" + "," + listTokens.get(getNextToken()).getLex());
                nextTokenAssembly();
                CMD();
            }
        }
        //* add $r1,$r2,$r3 *//
        else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(0))) {
            nextToken--;
            rr = nextToken - 2;
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg = idx;
                    }
                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                nextToken += 2;
                TERM();
                nextToken -= 1;
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                         if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg1 = idx;
                        }
                    }

                    writter(ExpressionAssembly.OP_ARIT.get(0) + " $t" + reg2 + ", $t" + reg + ", $t" + reg1);
                    nextTokenAssembly();
                } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $t" + reg2 + ", $t" + reg + ", " + listTokens.get(getNextToken()).getLex());
                    nextTokenAssembly();
                } 
            } 
            //* addi $r1,$re,Constante *//
            else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                nextToken--; 
                if(listTokens.get(getNextToken()).getLex().equals(Expression.TERMINADOR.get(0))){
                    nextTokenAssembly(); 
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +counter1+",$zero, "+listTokens.get(getNextToken()).getLex());
                }
                nextTokenAssembly(); 
                rr = nextToken - 2;
                
                if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(0))) {
                    nextTokenAssembly(); 
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg = idx;
                            }

                            if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        nextToken -= 2;
                        writter(ExpressionAssembly.OP_ARIT.get(4) + " $t" + reg2 + ", $t" + reg + ", " + listTokens.get(getNextToken()).getLex());
                        nextToken += 3;
                    }else if(Expression.isNumero(listTokens.get(getNextToken()).getLex())){
                       writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +(counter1+1)+",$zero, "+
                               listTokens.get(getNextToken()).getLex());
                       writter(ExpressionAssembly.OP_ARIT.get(0)+" $s"+tableSymbol.getTableSymbol().size()+" ,$s"
                               +counter1+", $s"+(counter1+1)); 
                       nextTokenAssembly(); 
                    }
                }
            }  
        } 
        //*sub $r1,$r2,$r3 *//
        else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(1))) {
            nextToken--;
            rr = nextToken - 2;
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg = idx;
                    }

                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                nextToken += 2;
                TERM();
                nextToken -= 1;
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg1 = idx;
                        }
                    }
                    writter(ExpressionAssembly.OP_ARIT.get(1) + " $t" + reg2 + ", $t" + reg + ", $t" + reg1);
                    nextTokenAssembly();
                } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $t" + tableSymbol.getTableSymbol().size() + ", $zero" + "," + listTokens.get(getNextToken()).getLex());
                    writter(ExpressionAssembly.OP_ARIT.get(1) + " $t" + reg2 + ", $t" + reg + ",$t" + tableSymbol.getTableSymbol().size());
                    nextTokenAssembly();
                }
            } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                nextToken--;
                if(listTokens.get(getNextToken()).getLex().equals(Expression.TERMINADOR.get(0))){
                    nextTokenAssembly(); 
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +counter1+",$zero, "+listTokens.get(getNextToken()).getLex());
                }
                nextTokenAssembly();
                rr = nextToken - 2;
                if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(1))) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg = idx;
                            }

                            if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        nextToken -= 2;
                        writter(ExpressionAssembly.OP_ARIT.get(4) + " $t" +tableSymbol.getTableSymbol().size() + ", $zero" + ", " +listTokens.get(getNextToken()).getLex());
                        writter(ExpressionAssembly.OP_ARIT.get(1) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                        nextToken += 3;
                    }else if(Expression.isNumero(listTokens.get(getNextToken()).getLex())){
                       writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +(counter1+1)+",$zero, "+listTokens.get(getNextToken()).getLex());
                       writter(ExpressionAssembly.OP_ARIT.get(1)+" $s"+tableSymbol.getTableSymbol().size()+" ,$s"+counter1+", $s"+(counter1+1)); 
                       nextTokenAssembly(); 
                    }
                } 
            }
        }
        CMD();  
    }
    
    public static void TERM() throws IOException {
        FATOR();
        int reg = 0, reg1 = 0, rr = 0, reg2 = 0, idx1 = 0;
        if (Expression.OP_ARIT.get(2).equals(listTokens.get(getNextToken()).getLex())) {
            nextToken--;
            rr = nextToken - 2;
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                        reg = idx;
                    }

                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                nextToken += 2;
                FATOR();
                nextToken -= 1;
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                            reg1 = idx;
                        }
                    }
                    writter(ExpressionAssembly.OP_ARIT.get(2) + " $t" + reg2 + ", $t" + reg + ", $t" + reg1);
                    nextTokenAssembly();
                } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                            reg = idx;
                        }
                    }
                    
                 
                    writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                    writter(ExpressionAssembly.OP_ARIT.get(2) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                    nextTokenAssembly();
                }
            } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                nextToken--;
                if(Expression.TERMINADOR.get(0).equals(listTokens.get(getNextToken()).getLex())){
                    nextTokenAssembly(); 
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +counter1+",$zero, "+listTokens.get(getNextToken()).getLex());
                }
                nextTokenAssembly(); 
                rr = nextToken - 2;
                if (Expression.OP_ARIT.get(2).equals(listTokens.get(getNextToken()).getLex())) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                                reg = idx;
                            }
                    
                            if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        nextToken -= 2;
                        writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                        writter(ExpressionAssembly.OP_ARIT.get(2) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                        nextToken += 3;
                    }
                    else if(Expression.isNumero(listTokens.get(getNextToken()).getLex())){
                       writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +(counter1+1)+",$zero, "+listTokens.get(getNextToken()).getLex());
                       writter(ExpressionAssembly.OP_ARIT.get(2)+" $s"+tableSymbol.getTableSymbol().size()+" ,$s"+counter1+", $s"+(counter1+1)); 
                       nextTokenAssembly(); 
                    }
                }
            }
            else if (Expression.KEYWORDS.get(15).equals(listTokens.get(getNextToken()).getLex())) {
            nextToken--;
            rr = nextToken - 2;
            if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                    && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                    if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                        reg = idx;
                    }

                    if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                        reg2 = idx;
                    }
                }
                nextToken += 2;
                FATOR();
                nextToken -= 1;
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                            reg1 = idx;
                        }
                    }
                    writter(ExpressionAssembly.OP_ARIT.get(3) + " $t" + reg2 + ", $t" + reg + ", $t" + reg1);
                    nextTokenAssembly();
                } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                            reg = idx;
                        }
                    }
                    writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                    writter(ExpressionAssembly.OP_ARIT.get(3) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                    nextTokenAssembly();
                }
            } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
                nextToken--;
                if(Expression.TERMINADOR.get(0).equals(listTokens.get(getNextToken()).getLex())){
                    nextTokenAssembly(); 
                    writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +counter1+",$zero, "+listTokens.get(getNextToken()).getLex());
                }
                nextTokenAssembly();
                rr = nextToken - 2;
               
                if (Expression.OP_ARIT.get(3).equals(listTokens.get(getNextToken()).getLex())) {
                    nextTokenAssembly();
                    if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                            && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                        for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                            if (tableSymbol.getTableSymbol().get(idx).getName().equals(listTokens.get(getNextToken()).getLex())) {
                                reg = idx;
                            }

                            if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                                reg2 = idx;
                            }
                        }
                        nextToken -= 2;
                        writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                        writter(ExpressionAssembly.OP_ARIT.get(3) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                        nextToken += 3;
                    }else if(Expression.isNumero(listTokens.get(getNextToken()).getLex())){
                       writter(ExpressionAssembly.OP_ARIT.get(4) + " $s" +(counter1+1)+",$zero, "+listTokens.get(getNextToken()).getLex());
                       writter(ExpressionAssembly.OP_ARIT.get(3)+" $s"+tableSymbol.getTableSymbol().size()+" ,$s"+counter1+", $s"+(counter1+1)); 
                       nextTokenAssembly(); 
                    }
                }
            }
        } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
            rr = nextToken - 2;
            nextTokenAssembly();
            if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(2))) {
                nextTokenAssembly();
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg = idx;
                        }

                        if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg2 = idx;
                        }
                    }
                    nextToken -= 2;
                    writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                    writter(ExpressionAssembly.OP_ARIT.get(2) + " $t" + reg2 + ", $t" + reg + ", $t" + (tableSymbol.getTableSymbol().size() + 1));
                    nextToken += 3;
                }
            } else if (listTokens.get(getNextToken()).getLex().equals(Expression.OP_ARIT.get(3))) {
                rr = nextToken - 2;
                nextTokenAssembly();
                if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                        && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
                    for (int idx = 0; idx < tableSymbol.getTableSymbol().size(); idx++) {
                        if (listTokens.get(getNextToken()).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg = idx;
                        }
                        if (listTokens.get(rr).getLex().equals(tableSymbol.getTableSymbol().get(idx).getName())) {
                            reg2 = idx;
                        }
                    }
                    nextToken -= 2;
                    writter("li $t" + tableSymbol.getTableSymbol().size() + "," + listTokens.get(getNextToken()).getLex());
                    writter(ExpressionAssembly.OP_ARIT.get(3) + " $t" + reg2 + ", $t" + reg + ", $t" + tableSymbol.getTableSymbol().size());
                    nextToken += 3;
                }
            }
        }
    }
    }
    
    public static void FATOR() throws IOException {
        if (Expression.TERMINADOR.get(0).equals(listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();
            BOOL();
            if (Expression.TERMINADOR.get(1).equals(listTokens.get(getNextToken()).getLex())){
                nextTokenAssembly();
            }
        } else if (!Expression.isKeywords(listTokens.get(getNextToken()).getLex()) 
                && Expression.isIdentificador(listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();
        } else if (Expression.isNumero(listTokens.get(getNextToken()).getLex())) {
            nextTokenAssembly();
        }
    }
    
}
