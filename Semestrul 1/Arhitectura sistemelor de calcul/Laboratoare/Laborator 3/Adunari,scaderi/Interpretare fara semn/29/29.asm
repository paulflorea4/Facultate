;a - byte, b - word, c - double word, d - qword
;d+c-b+(a-c)
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
        ;a->edx:eax
        mov eax,0
        mov edx,0
        mov al,[a]
        
        ;c->ecx:ebx
        mov ebx,[c]
        mov ecx,0
        
        ;(a-c)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        
        ;Salvez (a-c) pe stiva
        push eax
        push edx
        
        ;d->edx:eax
        mov eax,[d]
        mov edx,[d+4]
        
        ;d+c->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;b->ecx:ebx
        mov ecx,0
        mov ebx,0
        mov bx,[b]
        
        ;d+c-b->edx:eax
        sub eax,ebx
        sbb edx,ecx
        
        ;(a-c)->ecx:ebx
        pop ecx
        pop ebx
        
        ;d+c-b+(a-c)->edx:eax
        add eax,ebx
        adc edx,ecx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
