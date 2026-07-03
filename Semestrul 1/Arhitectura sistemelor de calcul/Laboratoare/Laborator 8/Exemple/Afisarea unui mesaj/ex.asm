; Codul de mai jos va afisa mesajul "Ana are 17 mere"
bits 32
global start        

; declararea functiilor externe folosite de program
extern exit, printf         ; adaugam printf ca functie externa            
import exit msvcrt.dll    
import printf msvcrt.dll    ; indicam asamblorului ca functia printf se gaseste in libraria msvcrt.dll
                          
segment data use32 class=data
; sirurile de caractere sunt de tip byte
format db "Ana are %d mere", 0  ; %d va fi inlocuit cu un numar
				; sirurile de caractere pt functiile C trebuie terminate cu valoarea 0
segment  code use32 class=code
    start:
        mov eax, 17
        
        ; vom apela printf(format, 17) => se va afisa: „Ana are 17 mere”
        ; punem parametrii pe stiva de la dreapta la stanga
        push dword eax
        push dword format ; ! pe stiva se pune adresa string-ului, nu valoarea
        call [printf]      ; apelam functia printf pentru afisare
        add esp, 4 * 2     ; eliberam parametrii de pe stiva; 4 = dimensiunea unui dword; 2 = nr de parametri

        ; exit(0)
        push dword 0      ; punem pe stiva parametrul pentru exit
        call [exit]       ; apelam exit pentru a incheia programul