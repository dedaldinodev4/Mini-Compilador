/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dedaldino.java.tac.app;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import com.dedaldino.java.tac.errors.Error;
import com.dedaldino.java.tac.generation.Assembly;
import com.dedaldino.java.tac.scanner.Scanner;
import com.dedaldino.java.tac.parser.Parser;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Dedaldino Daniel
 */
public class AppUI extends javax.swing.JFrame {

    /**
     * Creates new form AppUI
     */
    public AppUI() throws IOException {
        initComponents();
        OpenFile();
    }
    
    public void runApp() throws IOException {
        Error.cleanError();
        
       
        if(!this.jTextAreaCode.getText().equals("")) {
           
            //* Instante & Inits *//
            Scanner scanner = new Scanner();
            Parser parser = new Parser();
            Assembly assembly = new Assembly();
            
            
            //* Execute Scanner *//
            try {
                scanner.init("source.txt");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            scanner.exec();
            
            if(Error.getErrors().size() > 0){
                
                showTableError();
            } else {
                //* Execute Parser *//
                parser.init(scanner.getListTokens());
                parser.parserExecute();
                if(Error.getErrors().size() > 0) {
                    showTableError();
                } else {
                    
                    //* init & Execute Assembly *//
                    assembly.init(scanner.getListTokens(), parser.getTable());
                    assembly.execute();
                    
                    
                    //* Successful message *//
                    setAlertMessage("Execução Feita com Sucesso!", Color.GREEN);
                    if(parser.getTable().getTableSymbol().size() > 0) {
                        DefaultTableModel model = (DefaultTableModel) this.jTableVars.getModel();
      
                        Object rowDataVariable [] = new Object[3];
                
                        for(int i = 0; i< parser.getTable().getTableSymbol().size(); i++){
                
                            rowDataVariable[0] = parser.getTable().getTableSymbol().get(i).getName();
                            rowDataVariable[1] = parser.getTable().getTableSymbol().get(i).getDataType().toString();
                            rowDataVariable[2] = parser.getTable().getTableSymbol().get(i).getEscope();
            
                             model.addRow(rowDataVariable);
                        }
                    }
                    
                    writerAssemblyFile();
                    

                    
                }
            }
            
        }
        
        
    }
    
    public void  showTableError(){
        
         if(Error.getErrors().size() > 0){
            setAlertMessage("Erros durante a execução...", Color.red);
            this.jTextAreaOutput.append("\n");
            this.jTextAreaOutput.append("Tipo de Erro\tDescrição");
            for(int i = 0; i< Error.getErrors().size(); i++){
                this.jTextAreaOutput.append("\n");
                this.jTextAreaOutput.append(Error.getErrors().get(i).getTypeError()+"\t"+
                    Error.getErrors().get(i).getDescription());
            }
            
            
        
        }
        
        
    }
    
    public void setAlertMessage(String message, Color color) {
        this.jTextAreaOutput.setText(message);
        this.jTextAreaOutput.setForeground(color);
    }
    
    public String writeFile() {
        return this.jTextAreaCode.getText();
    }
    
    public void Write() throws IOException, BadLocationException {
        BufferedWriter writter;
        FileWriter file = new FileWriter("source.txt");
        
        writter = new BufferedWriter(file);
        //String[] lines = this.jTextAreaCode.getText().trim().split("\n\r");
        
       /* for (int idx = 0; idx < lines.length; idx++){
            writter.write(lines[idx]+ " ");
            writter.newLine();
        }*/
        
       writter.write(this.jTextAreaCode.getText(0, this.jTextAreaCode.getLineEndOffset(this.jTextAreaCode.getLineCount() - 1))+ " ");
           
        writter.close();
    }
    
    public void OpenFile() throws FileNotFoundException, IOException {
        BufferedReader read; 
        read = new BufferedReader(new FileReader("source.txt"));
        
        
        while(true) {
            String lines = read.readLine();
            
            if(lines == null)
                break;
            this.jTextAreaCode.append(lines + "\n");
        }   
    }
    
    public void writerAssemblyFile() throws FileNotFoundException, IOException {
        BufferedReader read; 
        read = new BufferedReader(new FileReader("source.asm"));
        
        while(true) {
            String lines = read.readLine();
            
            if(lines == null)
                break;
            this.jTextAreaAssembly.append(lines + "\n");
            
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaAssembly = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaCode = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableVars = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaOutput = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setForeground(new java.awt.Color(51, 51, 0));

        jTextAreaAssembly.setEditable(false);
        jTextAreaAssembly.setBackground(new java.awt.Color(32, 32, 32));
        jTextAreaAssembly.setColumns(20);
        jTextAreaAssembly.setForeground(new java.awt.Color(255, 255, 255));
        jTextAreaAssembly.setRows(5);
        jScrollPane1.setViewportView(jTextAreaAssembly);

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Source");

        jTextAreaCode.setBackground(new java.awt.Color(32, 32, 32));
        jTextAreaCode.setColumns(20);
        jTextAreaCode.setForeground(new java.awt.Color(80, 250, 123));
        jTextAreaCode.setRows(5);
        jScrollPane2.setViewportView(jTextAreaCode);

        jLabel6.setFont(new java.awt.Font("Raleway", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Assembly Output");

        jTableVars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome da Variável", "Tipo de dados"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableVars);
        if (jTableVars.getColumnModel().getColumnCount() > 0) {
            jTableVars.getColumnModel().getColumn(0).setResizable(false);
            jTableVars.getColumnModel().getColumn(0).setPreferredWidth(12);
            jTableVars.getColumnModel().getColumn(1).setResizable(false);
            jTableVars.getColumnModel().getColumn(1).setPreferredWidth(18);
        }

        jScrollPane6.setBackground(new java.awt.Color(32, 32, 32));
        jScrollPane6.setForeground(new java.awt.Color(255, 255, 255));

        jTextAreaOutput.setEditable(false);
        jTextAreaOutput.setBackground(new java.awt.Color(32, 32, 32));
        jTextAreaOutput.setColumns(20);
        jTextAreaOutput.setRows(5);
        jScrollPane6.setViewportView(jTextAreaOutput);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .addComponent(jLabel6))
                .addGap(38, 38, 38))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(426, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(135, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 830, 530));

        jPanel2.setBackground(new java.awt.Color(51, 51, 73));

        jButton1.setBackground(new java.awt.Color(80, 250, 123));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dedaldino/java/tac/views/run.png"))); // NOI18N
        jButton1.setText("Save & Run");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(241, 237, 104));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/dedaldino/java/tac/views/filenew.png"))); // NOI18N
        jButton2.setText("New File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(581, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setBorder(null);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(153, 153, 255));
        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Run");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Help");
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jTextAreaAssembly.setText("");
        this.jTextAreaCode.setText("");
        this.jTextAreaOutput.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Write();
            runApp();
         
                
        } catch (IOException ex) {
            Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AppUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableVars;
    private javax.swing.JTextArea jTextAreaAssembly;
    private javax.swing.JTextArea jTextAreaCode;
    private javax.swing.JTextArea jTextAreaOutput;
    // End of variables declaration//GEN-END:variables
}
