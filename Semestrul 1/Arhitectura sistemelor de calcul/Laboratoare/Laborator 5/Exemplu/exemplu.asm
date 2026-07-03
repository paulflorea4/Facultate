bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    n dd 100
    s dd 0
; our code starts here
segment code use32 class=code
    start:
        ;i=1
        ;s=0
        ;while(i<=n)
        ;{
        ;       s=s+i
        ;        i++
        ;}
        ;
        
        mov ecx,1  ;ecx=i=1
        xor eax,eax   ;eax=s=0
    .start_loop:
        cmp ecx,[n] ;(Comparam i cu n)
        jg .end_loop ;Daca i>n oprim bucla while
        add eax,ecx ;s=s+i
        inc ecx  ;i++
        jmp .start_loop
    .end_loop:
        mov [s],eax
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
