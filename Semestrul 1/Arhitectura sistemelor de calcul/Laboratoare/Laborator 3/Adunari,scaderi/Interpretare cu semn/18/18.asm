;a - byte, b - word, c - double word, d - qword
;(d-b)-a-(b-c)
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
    b resw 1
    c resd 1
    d resq 1

; our code starts here
segment code use32 class=code
    start:
        ;d->ecx:ebx
        mov ebx,[d]
        mov ecx,[d+4]
        
        ;b->edx:eax
        mov ax,[b]
        cwde
        cdq
        
        ;(d-b)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;a->edx:eax
        mov al,[a]
        cbw
        cwde
        cdq
        
        ;(d-b)-a->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;Salvez (d-b)-a pe stiva
        push ebx
        push ecx
        
        ;b->edx:eax
        mov ax,[b]
        cwde
        cdq
        
        ;c->ecx:ebx
        mov ebx,[c]
        cdq
        
        ;(b-c)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        
        ;(d-b)-a->ecx:ebx
        pop ecx
        pop ebx
        
        ;(d-b)-a-(b-c)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
