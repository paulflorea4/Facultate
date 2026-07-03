;a,b,c-byte; d-doubleword; x-qword
;(a+b)/(c-2)-d+2-x
bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
       a resb 1
       b resb 1
       c resb 1
       d resd 1
       x resq 1

; our code starts here
segment code use32 class=code
    start:
        ;(a+b)->ax
        mov al,[a]
        cbw
        add al,[b]
        
        ;(c-2)->bl
        mov bl,[c]
        sub bl,2
        
        ;(a+b)/(c-2)->ecx:ebx
        idiv bl
        cbw
        cwde
        cdq
        mov ebx,eax
        mov ecx,edx
        
        ;d->edx:eax
        mov eax,[d]
        cdq
        
        ;(a+b)/(c-2)-d+2->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        add ebx,2
        
        ;x->edx:eax
        mov eax,[x]
        mov edx,[x+4]
        
        ;(a+b)/(c-2)-d+2-x->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
