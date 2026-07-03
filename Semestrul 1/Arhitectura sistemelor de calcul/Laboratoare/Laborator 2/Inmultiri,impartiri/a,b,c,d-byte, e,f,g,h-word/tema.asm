bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db 4h
    b db 2h
    c db 1h
    d db 3h
    
    e dw 8h
    f dw 9h
    g dw 7h
    h dw 5h
; our code starts here
segment code use32 class=code
    start:
        ;11. (e+f)*(2*a+3*b)
        ; mov BX,[e]
        ; add BX,[f]
        ; mov AL,2
        ; mul BYTE [a]
        ; mov CX,AX
        ; mov AL,3
        ; mul BYTE [b]
        ; add AX,CX
        ; mul BX
        
        ;14. a*d*e/(f-5)
        ; mov AL,[a]
        ; mul BYTE [d]
        ; mul WORD [e]
        ; mov BX,[f]
        ; sub BX,5
        ; div BX
        
        ;17. h/a + (2 + b) + f/d – g/c
        ; mov AX,[h]
        ; mov BL,[a]
        ; div BL
        ; mov BL,[b]
        ; add BL,2
        ; add BL,AL
        ; mov AX,[f]
        ; div BYTE [d]
        ; add BL,AL
        ; mov AX,[g]
        ; div BYTE [c]
        ; sub BL,AL
        
        ;26. (e+g-2*b)/c
        ; mov AL,2
        ; mul BYTE [b]
        ; mov BX,AX
        ; mov AX,[e]
        ; add AX,[g]
        ; sub AX,BX
        ; div BYTE [c]
        
        ;27. [(e+f-g)+(b+c)*3]/5
        mov AL,3
        mov AH,[b]
        add AH,[c]
        mul AH
        mov BX,[e]
        add BX,[f]
        sub BX,[g]
        add AX,BX
        mov BL,5
        div BL
        exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
