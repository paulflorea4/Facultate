bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
; our code starts here
segment code use32 class=code
    start:
        ; c-(a+d)+(b+d)
        ; a - byte, b - word, c - double word, d - qword - Interpretare fara semn
        ;a->edx:eax
        mov eax,0
        mov al,BYTE[a]
        mov edx,0
        
        ;d->ecx:ebx
        mov ebx,DWORD[d]
        mov ecx,DWORD[d+4]
        
        ;a+d->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;c->ecx:ebx
        mov ebx,DWORD[c]
        mov ecx,0
        
        ;c-(a+d)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        
        ;Salvez c-(a+d) pe stiva
        push ebx
        push ecx
        
        ;b->edx;eax
        mov edx,0
        mov eax,DWORD[b]
        
        ;d->ecx:ebx
        mov ebx,DWORD[d]
        mov ecx,DWORD[d+4]
        
        ;b+d->edx:eax
        add eax,ebx
        add edx,ecx
        
        ;c-(a+d)->ecx:ebx
        pop ecx
        pop ebx
        
        ;c-(a+d)+(b+d)->edx:eax
        add eax,ebx
        adc edx,ecx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
