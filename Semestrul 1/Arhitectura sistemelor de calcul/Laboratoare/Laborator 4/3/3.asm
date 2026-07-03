bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
;3.Se dau cuvintele A si B. Sa se obtina dublucuvantul C:
    ; bitii 0-2 ai lui C coincid cu bitii 12-14 ai lui A
    ; bitii 3-8 ai lui C coincid cu bitii 0-5 ai lui B
    ; bitii 9-15 ai lui C coincid cu bitii 3-9 ai lui A
    ; bitii 16-31 ai lui C coincid cu bitii lui A
    
    A dw 7483h
    B dw 2467h
    C resd 1

; our code starts here
segment code use32 class=code
    start:
        mov dx,[A]
        mov ax,0
        
        mov bx,[A]
        and bx,0111000000000000b
        mov cl,4
        rol bx,cl
        or ax,bx
        
        mov bx,[B]
        and bx,0000000000111111b
        mov cl,3
        rol bx,cl
        or ax,bx
        
        mov bx,[A]
        and bx,0000001111111000b
        mov cl,6
        rol bx,cl
        or ax,bx
        
        push dx
        push ax
        pop eax
        mov [C],eax
        ;
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
