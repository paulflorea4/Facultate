; a-word; b,c,d-byte; e-doubleword; x-qword
;(a*2+b/2+e)/(c-d)+x/a
bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a resw 1
    b resb 1
    c resb 1
    d resb 1
    e resd 1
    x resq 1

; our code starts here
segment code use32 class=code
    start:
        ;a*2->dx:ax
        mov ax,[a]
        mov cx,2
        mul cx
        
        ;Salvez a*2 pe stiva
        push ax
        push dx
        
        ;b/2->dx:ax
        mov al,[b]
        mov ah,0
        mov dx,0
        mov bx,2
        div bx
        mov dx,0
        
        ;a*2->cx:bx
        pop cx
        pop bx
        
        ;a*2+b/2->cx:bx
        add bx,ax
        adc cx,dx
        
        ;e->dx:ax
        mov ax,[e]
        mov dx,[e+2]
        
        ;a*2+b/2+e->dx:ax
        add ax,bx
        adc dx,cx
        
        ;c->cx
        mov cl,[c]
        mov ch,0
        
        ;b->bx
        mov bl,[d]
        mov bh,0
        
        ;(c-d)->cx
        sub cx,bx
        
        ;(a*2+b/2+e)/(c-d)->ax
        div cx
        mov dx,0
        push dx
        push ax
        pop ecx
        
        ;x->edx:eax
        mov eax,[x]
        mov edx,[x+4]
        
        ;a->ebx
        mov ebx,0
        mov bx,[a]
        
        ;x/a->eax
        div ebx
        
        ;(a*2+b/2+e)/(c-d)+x/a->eax
        add eax,ecx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
