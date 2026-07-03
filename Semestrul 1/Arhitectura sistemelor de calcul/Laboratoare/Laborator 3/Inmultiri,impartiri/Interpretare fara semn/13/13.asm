;a,c,d-byte; b-doubleword; x-qword
;x-(a+b+c*d)/(9-a)
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
    ; b resd 1
    ; c resb 1
    ; d resb 1
    ; x resq 1
    
    a db 5h
    b dd 2h
    c db 1h
    d db 1h
    x dq 4h

; our code starts here
segment code use32 class=code
    start:
        ;c*d->dx:ax
        mov dx,0
        mov al,[c]
        mul byte [d]
        
        ;b+c*d->dx:ax
        add ax,[b]
        adc dx,[b+2]
        
        ;a->cx:bx
        mov cx,0
        mov bx,0
        mov bl,[a]
        
        ;a+b+c*d->dx:ax
        add ax,bx
        adc dx,cx
        
        ;9-a->bx
        mov bx,9
        mov cx,0
        mov cl,[a]
        sub bx,cx
        
        ;(a+b+c*d)/(9-a)->edx:eax
        div bx
        mov cx,ax
        mov eax,0
        mov ax,cx
        mov edx,0
        
        ;x->ecx:ebx
        mov ebx,[x]
        mov ecx,[x+4]
        
        ;x-(a+b+c*d)/(9-a)->ecx:ebx
        sub ebx,eax
        sbb ecx,edx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
