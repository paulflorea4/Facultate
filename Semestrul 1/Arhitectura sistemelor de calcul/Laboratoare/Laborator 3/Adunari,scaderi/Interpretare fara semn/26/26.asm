;a - byte, b - word, c - double word, d - qword
;(c-b+a)-(d+a)
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
        ;c->eax
        mov eax,[c]
        
        ;b->ebx
        mov ebx,0
        mov bx,[b]
        
        ;a->ecx
        mov ecx,0
        mov cl,[a]
        
        ;(c-b+a)->eax
        sub eax,ebx
        adc eax,ecx
        
        ;Salvez pe stiva (c-b+a)
        push eax
        
        ;d->edx:eax
        mov eax,[d]
        mov edx,[d+4]
        
        ;a->ecx:ebx
        mov ebx,0
        mov ecx,0
        mov bl,[a]
        
        ;(d+a)->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;(c-b+a)->ecx:ebx
        pop ebx
        mov ecx,0
        
        ;(c-b+a)-(d+a)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
