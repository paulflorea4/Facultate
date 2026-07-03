;a,b-byte; c-word; e-doubleword; x-qword
;(a+b*c+2/c)/(2+a)+e+x
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
       b resb 1
       c resw 1
       e resd 1
       x resq 1

; our code starts here
segment code use32 class=code
    start:
        ;b*c->cx:bx
        mov al,[b]
        cbw
        imul word [c]
        mov bx,ax
        mov cx,dx
        
        ;2/c->dx:ax
        mov ax,2
        cwd
        idiv word [c]
        cwd
        
        ;b*c+2/c->cx:bx
        add bx,ax
        adc cx,dx
        
        ;a->dx:ax
        mov al,[a]
        cbw
        cwd
        
        ;a+b*c+2/c->cx:bx
        add bx,ax
        adc cx,dx
        
        ;2+a->stiva
        mov al,[a]
        cbw
        add ax,2
        push ax
        
        ;(a+b*c+2/c)/(2+a)->ecx:ebx
        mov dx,cx
        mov ax,bx
        pop bx
        idiv bx
        cwde
        cdq
        mov ebx,eax
        mov ecx,edx
        
        ;e->edx:eax
        mov eax,[e]
        cdq
        
        ;(a+b*c+2/c)/(2+a)+e->ecx:ebx
        add ebx,eax
        adc ecx,edx
        
        ;x->edx:eax
        mov eax,[x]
        mov edx,[x+4]
        
        ;(a+b*c+2/c)/(2+a)+e+x->ecx:ebx
        add ebx,eax
        adc ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
