.data
#correto
a:.word 0 
b:.word 0 
c:.word 0 
.text
.globl main
main:
lw $t1,a
lw $t2,b
lw $t3,c
addi $t1, $zero,1
