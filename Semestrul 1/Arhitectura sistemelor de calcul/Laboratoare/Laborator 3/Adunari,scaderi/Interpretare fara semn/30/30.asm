;a - byte, b - word, c - double word, d - qword
;(b+c+a)-(d+c+a)
bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; a resb 1
    ; b resw 1
    ; c resd 1
    ; d resq 1
    
    a db 1h
    b dw 2h
    c dd 3h
    d dq 4h

; our code starts here
segment code use32 class=code
    start:
        ;c->edx:eax
        mov eax,[c]
        mov edx,0
        
        ;a->ecx:ebx
        mov ecx,0
        mov ebx,0
        mov bl,[a]
        
        ;c+a->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;b->ecx:ebx
        mov ecx,0
        mov ebx,0
        mov bx,[b]
        
        ;(b+c+a)->ecx:ebx
        add ebx,eax
        adc ecx,eax
        
        ;Salvez pe stiva (b+c+a)
        push ebx
        push ecx
        
        ;d->ecx:ebx
        mov ebx,[d]
        mov ecx,[d+4]
        
        ;(d+c+a)->ecx:ebx
        add ebx,eax
        adc ecx,eax
        
        ;(b+c+a)->edx:eax
        pop edx
        pop eax
        
        ;(b+c+a)-(d+c+a)->edx:eax
        sub eax,ebx
        sbb edx,ecx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
