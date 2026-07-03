bits 32 

global start        

extern exit,printf,scanf              
import exit msvcrt.dll  
import printf msvcrt.dll
import scanf msvcrt.dll  

%include "concatenare.asm"
;3.Se dau doua siruri continand caractere. Sa se calculeze si sa se afiseze rezultatul concatenarii tuturor caracterelor tip cifra zecimala din cel de-al doilea sir dupa cele din primul sir si invers, rezultatul concatenarii primului sir dupa al doilea.             
segment data use32 class=data
    s1 times 100 db 0
    s2 times 100 db 0
    format db "%s",0
    Rez1 times 200 db 0
    Rez2 times 200 db 0
    mesaj db "Cf",0
    spatiu db 10,0
segment code use32 class=code
        
    start:
        push dword s1
        push dword format
        call [scanf]
        add esp,4*2
        
        push dword s2
        push dword format
        call [scanf]
        add esp,4*2
        
        push dword Rez1
        push dword s2
        push dword s1
        call concatenare
        add esp,4*3
        
        push dword Rez2
        push dword s1
        push dword s2
        call concatenare
        add esp,4*3
        
        push dword Rez1
        call [printf]
        add esp,4
        
        push dword spatiu
        call [printf]
        add esp,4
        
        push dword Rez2
        call [printf]
        add esp,4
       
        ; exit(0)
        push    dword 0      
        call    [exit]       
