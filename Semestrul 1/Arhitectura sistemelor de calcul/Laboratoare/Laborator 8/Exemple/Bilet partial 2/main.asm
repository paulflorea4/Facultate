bits 32

global start

extern exit,fopen,fclose,fread,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "input.txt",0
    mod_access db "r",0
    descriptor_file dd -1
    max equ 100
    text times max db 0
    output_format db "%s",0
segment code use32 class=code
    start:
        push dword mod_access
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
        
        push dword [descriptor_file]
        push dword max
        push dword 1
        push dword text
        call [fread]
        add esp,4*4
        
        mov esi,text
        mov edi,text
        cld
    repeta:
        lodsb
        cmp al,0
        je afara
        cmp al,'z'
        ja alt_caracter
        cmp al,'a'
        ja litera
        cmp al,'A'
        jb alt_caracter
        cmp al,'Z'
        jb litera
        jmp alt_caracter
    litera:
        sub al,2
    alt_caracter:
        stosb
        jmp repeta
        
    afara:
        push dword text
        push dword output_format
        call [printf]
        add esp,4*2
        
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]