;a,b-byte; c-word; e-doubleword; x-qword
;(a-2)/(b+c)+a*c+e-x
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
    ; b resb 1
    ; c resw 1
    ; e resd 1
    ; x resq 1
    

; our code starts here
segment code use32 class=code
    start:
        ;a-2->dx:ax
        mov ax,0
        mov al,[a]
        mov dx,0
        sub ax,2
        
        ;b+c->bx
        mov bx,0
        mov bl,[b]
        add bx,[c]

        ;(a-2)/(b+c)->cx:bx
        div bx
        mov bx,ax
        mov cx,0
 
        ;a->cx
        mov ax,0
        mov al,[a]
        
        ;a*c->dx:ax
        mul word [c]
        
        ;(a-2)/(b+c)+a*c->dx:ax
        add ax,bx
        adc dx,cx
        
        ;(a-2)/(b+c)+a*c->edx:eax
        push dx
        push ax
        pop eax
        mov edx,0
        
        ;e->ecx:ebx
        mov ebx,[e]
        mov ecx,0
        
        ;(a-2)/(b+c)+a*c+e->edx:eax
        add eax,ebx
        adc edx,ecx
        
        ;x->ecx:ebx
        mov ebx,[x]
        mov ecx,[x+4]
        
        ;(a-2)/(b+c)+a*c+e-x->edx:eax
        sub eax,ebx
        sbb edx,ecx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
