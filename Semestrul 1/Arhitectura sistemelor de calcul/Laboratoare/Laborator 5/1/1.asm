bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    S db 1,2,3,4,5,15,15
    len equ $-S
    D times len-1 db 0

; our code starts here
segment code use32 class=code
    start:
        mov ecx,len-1
        mov esi,0
        jecxz Sfarsit
        repeta: 
            mov al,[S+esi]
            mov ah,[S+esi+1]
            mul ah
            mov [D+esi],al
            inc esi
        loop repeta
        Sfarsit:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
