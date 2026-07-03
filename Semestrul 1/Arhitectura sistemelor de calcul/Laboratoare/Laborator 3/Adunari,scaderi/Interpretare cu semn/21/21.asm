;a - byte, b - word, c - double word, d - qword
;d-a+(b+a-c)
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
        mov ax,[b]
        cwde
        cdq
        
        ;b->ecx:ebx
        mov ebx,eax
        mov ecx,edx
        
        ;a->edx:eax
        mov al,[a]
        cbw
        cwde
        cdq
        
        ;b+a->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;c->edx:eax
        mov eax,[c]
        cdq
        
        ;(b+a-c)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;Salvez (b+a-c) pe stiva
        push ebx
        push ecx
        
        ;a->edx:eax
        mov al,[a]
        cbw
        cwde
        cdq
        
        ;d->ecx:ebx
        mov ebx,[d]
        mov ecx,[d+4]
        
        ;d-a->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;(b+a-c)->edx:eax
        pop edx
        pop eax
        
        ;d-a+(b+a-c)->ecx:ebx
        add ebx,eax
        adc ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
