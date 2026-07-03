bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,scanf,printf          
import exit msvcrt.dll    
import scanf msvcrt.dll
import printf msvcrt.dll                       

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ;Se citesc de la tastatura numere (in baza 10) pana cand se introduce cifra 0. Determinaţi şi afişaţi cel mai mic număr dintre cele citite.
    a resd 1
    mesaj db "Dati un numar intreg: ",0
    format db "%d",0
   
    format_afisare db "Cel mai mic numar: %d"
; our code starts here
segment code use32 class=code
    start:
    or ebx,0xffffffff
    citire:
        push dword mesaj
        call [printf]
        add esp,4*1
        
        push dword a
        push dword format
        call [scanf]
        add esp,4*2
        
        cmp dword [a],0
        je final
        
        cmp [a],ebx
        jl mai_mic
        jmp citire
        mai_mic:
        mov ebx,[a]
        jmp citire
        
    final:
        push ebx
        push dword format_afisare
        call [printf]
        add esp,4*2
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
