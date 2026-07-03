;a - byte, b - word, c - double word, d - qword
;a + b + c + d - (a + b)
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
        
        ;b->edx:eax
        mov ax,[b]
        cwde
        cdq
        
        ;(a+b)->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;Salvez (a+b) pe stiva
        push ebx
        push ecx
        
        ;c->edx:eax
        mov eax,[c]
        cdq
        
        ;a+b+c->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;d->edx:eax
        mov eax,[d]
        mov edx,[d+4]
        
        ;a+b+c+d->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;(a+b)->edx:eax
        pop edx
        pop eax
        
        ;a+b+c+d-(a+b)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
