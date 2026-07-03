bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    
    a db 1h
    b db 2h
    c db 3h
    d dw 10h

; our code starts here
segment code use32 class=code
    start:
        ;1. ((a+b-c)*2 + d-5)*d
        ; mov AL,[a]
        ; add AL,[b]
        ; sub AL,[c]
        ; mov BL,2
        ; mul BL
        ; add AX,[d]
        ; sub AX,5
        ; mov BX,[d]
        ; mul BX
        
        ;2. d*(d+2*a)/(b*c)
        ; mov AL,[a]
        ; mov BL,2
        ; mul BL
        ; add AX,[d]
        ; mov BX,[d]
        ; mul BX
        ; mov BX,AX
        ; mov AL,[b]
        ; mul BYTE [c]
        ; mov CX,AX
        ; mov AX,BX
        ; div CX
        
        ;4. –a*a + 2*(b-1) – d
        ; mov AL,[b]
        ; sub AL,1
        ; mov AH,2
        ; mul AH
        ; mov BX,AX
        ; mov AL,[a]
        ; mul BYTE [a]
        ; sub BX,AX
        ; sub BX,[d]
        
        ;27. d/[(a+b)-(c+c)]
        ; mov BL, [a] 
        ; add BL, [b] 
        ; mov BH, [c] 
        ; add BH, [c] 
        ; sub BL, BH  
        ; mov AX, [d] 
        ; div BL 
        
        ;29. [d-(a+b)*2]/c
        mov AL,2
        mov AH,[a]
        add AH,[b]
        mul AH
        mov BX,AX
        mov AX,[d]
        sub AX,BX
        mov BL,[c]
        div BL
     
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
