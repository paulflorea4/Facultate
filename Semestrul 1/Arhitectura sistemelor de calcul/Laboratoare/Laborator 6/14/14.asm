bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functionxs

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    S dd 12345607h,1A2B3C15h
    len equ $-S
    D times len db 0
; our code starts here
segment code use32 class=code
    start:
        mov ecx,len
        cld
        mov esi,S
        mov edi,D
        rep movsb
        mov esi,D
        mov ecx,len
        dec ecx
        repeta_1:
            push ecx
            mov al,[esi]
            mov ebx,1
            repeta_2:
                mov dl,[esi+ebx]
                cmp al,dl
                jb skip_swap
                mov [esi+ebx],al
                mov [esi],dl
                mov al,dl
            skip_swap:
                inc ebx
                cmp ebx,len
                je next
                loop repeta_2
        next:
            inc esi
            pop ecx
            loop repeta_1
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
