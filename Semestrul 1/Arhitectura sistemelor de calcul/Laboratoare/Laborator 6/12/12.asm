bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    A dw 1122h,3344h,5566h,7788h
    len equ ($-A)/2
    B1 times len db 0
    B2 times len db 0

; our code starts here
segment code use32 class=code
    start:
        mov ecx,len
        jecxz final
        cld
        mov esi,A
        mov edi,B1
        mov ebx,B2
        repeta:
            movsb
            lodsb
            push edi
            mov edi,ebx
            stosb
            mov ebx,edi
            pop edi
            loop repeta
        final:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
