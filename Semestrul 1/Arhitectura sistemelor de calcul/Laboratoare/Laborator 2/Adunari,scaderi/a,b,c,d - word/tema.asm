bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a DW 3h
    b DW 4h
    c DW 2h
    d DW 1h

; our code starts here
segment code use32 class=code
    start:
        ;1. (c+b+a)-(d+d)
        mov AX,[c]
        add AX,[b]
        add AX,[a]
        mov BX,[d]
        add BX,[d]
        sub AX,BX
        
        ;6. c-(d+a)+(b+c)
        mov AX,[a]
        mov BX,[d]
        add BX,[a]
        sub AX,BX
        mov BX,[b]
        add BX,[c]
        add AX,BX
        
        ;12. d-(a+b)-(c+c)
        mov AX,[d]
        mov BX,[a]
        add BX,[b]
        sub AX,BX
        mov BX,[c]
        add BX,[c]
        sub AX,BX
        
        ;19. b+a-(4-d+2)+c+(a-b)
        mov AX,[b]
        add AX,[a]
        mov BX,4
        sub BX,[d]
        add BX,2
        sub AX,BX
        add AX,[c]
        mov BX,[a]
        sub BX,[b]
        add AX,BX
        
        ;21. a-c+d-7+b-(2+d)
        mov AX,[a]
        sub AX,[c]
        add AX,[d]
        sub AX,7
        add AX,[b]
        mov BX,2
        add BX,[d]
        sub AX,BX
        
        ;27 a+b-(c+d)+100h
        mov AX,[a]
        add AX,[b]
        mov BX,[c]
        add BX,[d]
        sub AX,BX
        add AX,100h
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
