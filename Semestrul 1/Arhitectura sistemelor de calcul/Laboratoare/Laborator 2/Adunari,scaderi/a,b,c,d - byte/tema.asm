bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a DB 3h
    b DB 4h
    c DB 2h
    d DB 1h
 
; our code starts here
segment code use32 class=code
    start:
        ;1. c-(a+d)+(b+d)
        mov AL,[c]
        mov BL,[a]
        add BL,[d]
        sub AL,BL
        mov BL,[b]
        add BL,[d]
        add AL,BL
        
        ;3. (c+d)-(a+d)+b
        mov AL,[c]
        add AL,[d]
        mov BL,[a]
        add BL,[d]
        sub AL,BL
        add AL,[b]
        
        ;4. (a-b)+(c-b-d)+d
        mov AL,[a]
        sub AL,[b]
        mov BL,[c]
        sub BL,[b]
        sub BL,[d]
        add AL,BL
        add AL,[d]
        
        ;11. (a+c-d)+d-(b+b-c)
        mov AL,[a]
        add AL,[c]
        sub AL,[d]
        add AL,[d]
        mov BL,[b]
        add BL,[b]
        sub BL,[c]
        sub AL,BL
        
        ;12. 2-(c+d)+(a+b-c)
        mov AL,2
        mov BL,[c]
        add BL,[d]
        sub AL,BL
        mov BL,[a]
        add BL,[b]
        sub BL,[c]
        add AL,BL
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
