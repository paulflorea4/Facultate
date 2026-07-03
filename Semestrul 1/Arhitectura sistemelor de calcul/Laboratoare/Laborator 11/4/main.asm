bits 32 

global start

extern exit,printf

import exit msvcrt.dll
import printf msvcrt.dll
%include "baza2.asm"
segment data use32 class=data
    s db 13,23,29,32,17,18
    len equ $-s
    output_format_hex db "%d:%x",13,10,0
    output_format_bin db "%d:%s",13,10,0
    hex_msg db "Valorile in baza 16",13,10,0
    bin_msg db "Valorile in baza 2",13,10,0
    biti dd 0
segment code use32 class=code
    start:
        push dword hex_msg
        call [printf]
        add esp,4*1
        
        mov ecx,len
        jecxz final
        cld
        mov esi,s
        
    loop1:
        push ecx
        xor eax,eax
        lodsb
        push eax
        push eax
        push dword output_format_hex
        call [printf]
        add esp,4*3
        pop ecx
        loop loop1
        
        push dword bin_msg
        call [printf]
        add esp,4*1
        
        mov ecx,len
        cld
        mov esi,s
    loop2:
        push ecx
        xor eax,eax
        lodsb
        push eax
        mov edx,eax
        
        push dword biti
        call base2
            
        pop eax
        
        push dword biti
        push eax
        push dword output_format_bin
        call [printf]
        add esp,4*3
        
        pop ecx
        
        loop loop2
        
    final:
        push dword 0
        call [exit]