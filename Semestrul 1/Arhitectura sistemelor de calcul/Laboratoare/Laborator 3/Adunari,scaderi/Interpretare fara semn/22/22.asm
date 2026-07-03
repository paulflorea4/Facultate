;a - byte, b - word, c - double word, d - qword
;(d+c) - (c+b) - (b+a)
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
        ;d->edx:eax
        mov eax,[d]
        mov edx,[d+4]
        
        ;c->ecx:ebx
        mov ebx,[c]
        mov ecx,0
        
        ;(d+c)->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;Salvez pe stiva (d+c)
        push eax
        push edx
        
        ;b->edx:eax
        mov eax,0
        mov edx,0
        mov ax,[b]
        
        ;(b+c)->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;(d+c)->edx:eax
        pop edx
        pop eax
        
        ;(d+c) - (c+b)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        
        ;b->ebx
        mov ebx,0
        mov bx,[b]
        
        ;a->ecx
        mov ecx,0
        mov cl,[a]
        
        ;(b+a)->ecx:ebx
        add ebx,ecx
        mov ecx,0
        
        ;(d+c) - (c+b) - (b+a)->edx:eax
        sub eax,ebx
        sbb edx,ecx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
