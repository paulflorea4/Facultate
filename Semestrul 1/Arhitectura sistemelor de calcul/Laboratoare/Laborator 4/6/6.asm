bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; Se da cuvantul A. Sa se obtina numarul intreg n reprezentat de bitii 0-2 ai lui A. Sa se obtina apoi in B cuvântul rezultat prin rotirea spre dreapta (fara carry) a lui A cu n pozitii. Sa se obtina dublucuvantul C:
        ; bitii 8-15 ai lui C sunt 0
        ; bitii 16-23 ai lui C coincid cu bitii lui 2-9 ai lui B
        ; bitii 24-31 ai lui C coincid cu bitii lui 7-14 ai lui A
        ; bitii 0-7 ai lui C sunt 1
        
        A dw 3425h
        n resb 1
        B resw 1
        C resd 1

; our code starts here
segment code use32 class=code
    start:
        mov cl,[A]
        and cl,0111b
        mov [n],cl
        
        mov ax,[A]
        ror ax,cl
        mov [B],ax
        
        mov dx,0
        mov ax,0
        
        and ax,1111111100000000b
        
        or ax,0000000011111111b
        
        mov bx,[B]
        and bx,0000001111111100b
        mov cl,2
        ror bx,2
        or dx,bx
        
        mov bx,[A]
        and bx,0111111110000000b
        mov cl,1
        rol bx,cl
        or dx,bx
        
        mov [C],ax
        mov [C+2],dx
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
