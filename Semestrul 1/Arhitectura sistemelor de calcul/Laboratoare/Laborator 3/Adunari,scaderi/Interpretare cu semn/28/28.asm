;a - byte, b - word, c - double word, d - qword
;c+d-a-b+(c-a)
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
        mov al,[a]
        cbw
        cwde
        cdq
        
        ;a->ecx:ebx
        mov ebx,eax
        mov ecx,edx
        
        ;c->edx:eax
        mov eax,[c]
        cdq
        
        ;(c-a)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        
        ;Salvez (c-a) pe stiva
        push eax
        push edx
        
        ;b->edx:eax
        mov ax,[b]
        cwde
        cdq
        
        ;d->ecx:ebx
        mov ebx,[d]
        mov ecx,[d+4]
        
        ;d-b->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;(c-a)->edx:eax
        pop edx
        pop eax
        
        ;c+d-a-b->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;c+d-a-b+(c-a)->ecx:ebx
        add ebx,eax
        adc ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
