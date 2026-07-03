;a - byte, b - word, c - double word, d - qword 
;(c-a) + (b - d) +d
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
        ;b->edx:eax
        mov eax,0
        mov ax,[b]
        mov edx,0
        ;d->ecx:ebx
        mov ebx,[d]
        mov ecx,[d+4]
        ;(b-d)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        ;(b-d)+d->edx:eax
        add eax,ebx
        adc edx,ecx
        ;Salvez (b-d)+d pe stiva
        push eax
        push edx
        ;c->ecx:ebx
        mov ecx,0
        mov ebx,[c]
        ;a->edx:eax
        mov eax,0
        mov edx,0
        mov al,[a]
        ;(c-a)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        ;(b-d)+d->edx:eax
        pop edx
        pop eax
        ;(c-a)+(b-d)+d->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
