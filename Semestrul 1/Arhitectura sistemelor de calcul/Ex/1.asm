bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        
extern printf
; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll
import printf msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    ; x dw -256,256h
    ; y dw 256|-256,256h&256
    ; z db $-z,y-x
      ; db 'y'-'x','y-x'
    ; a db 512>>2,-512<<2
    ; b dw z-a,!(z-a)
    ; c dd ($-b)+(d-$)
    ; d db -128,128^(~128)
    ; times 2 resw 6
    ; times 2 dd 1234h,5678h
; our code starts here
segment code use32 class=code
    start:
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
