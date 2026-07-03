bits 32

global start

extern exit,fopen,fclose,fprintf,scanf,printf,fread
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll
import fread msvcrt.dll

segment data use32 class=data
    file_name times 20 db 0
    c dd 0
    n dd 0
    descriptor_file dd -1
    mod_acces db "r",0
    msg db "Cititi numele fisierului,un caracter si un numar:",0
    format db "%s %c %d",0
    ok dd 1
    max equ 100
    text times max db 0
    output_format_true db "Numarul de aparitii al caracterului %c este egal cu numarul %d citit.",0
    output_format_false db "Numarul de aparitii al caracterului %c nu este egal cu numarul %d citit.",0
segment code use32 class=code
    start:
        push dword msg
        call [printf]
        add esp,4
        
        push dword n
        push dword c
        push dword file_name
        push dword format
        call [scanf]
        add esp,4*4
        
        push dword mod_acces
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
        
        push dword [descriptor_file]
        push dword 100
        push dword 1
        push dword text
        call [fread]
        add esp,4*4
        
        mov esi,text
        cld
        xor ebx,ebx
    repeta:
        lodsb
        cmp al,0
        je sfarsit
        cmp al,byte [c]
        jne urmatorul
        inc ebx
    urmatorul:
        jmp repeta
    sfarsit: 
        cmp ebx,[n]
        je afisare
        push dword [n]
        push dword [c]
        push dword output_format_false
        call [printf]
        add esp,4*3
        jmp peste
    afisare:
        push dword [n]
        push dword [c]
        push dword output_format_true
        call [printf]
        add esp,4*3
    peste:
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]