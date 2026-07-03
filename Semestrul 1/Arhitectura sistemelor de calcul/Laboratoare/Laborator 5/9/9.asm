bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    S db 1,2,4,6,10,20,25   
    len equ $-S
    d times len-1 db 0

; our code starts here
segment code use32 class=code
    start:
        mov ecx,len-1
        mov esi,0
        jecxz Sfarsit
        repeta:
            mov al,[S+esi]
            mov bl,[S+esi+1]
            sub bl,al
            mov [d+esi],bl
            inc esi
        loop repeta
        Sfarsit:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
