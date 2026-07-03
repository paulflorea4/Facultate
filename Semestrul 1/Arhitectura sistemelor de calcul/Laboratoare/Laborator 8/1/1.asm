bits 32

global start

extern exit,fopen,fread,fclose,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import fclose msvcrt.dll
import printf msvcrt.dll

;1.Se da un fisier text. Sa se citeasca continutul fisierului, sa se contorizeze numarul de vocale si sa se afiseze aceasta valoare. Numele fisierului text este definit in segmentul de date.
segment data use32 class=data
    file_name db "input.txt",0
    mod_access db "r",0
    file_descriptor dd -1
    vocale db "aeiouAEIOU",0
    max equ 100
    text times max db 0
    output_format db "Numarul de vocale:%d",0
    output db "%s",0
segment code use32 class=code
    start:
        push dword mod_access
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [file_descriptor],eax
        cmp eax,0
        
        je eroare
        
        push dword [file_descriptor]
        push dword max
        push dword 1
        push dword text
        call [fread]
        add esp,4*4
        
        mov esi,text
        cld
        mov ebx,0
    repeta:
        lodsb
        cmp al,0
        je final
        xor ecx,ecx
        mov ecx,10
        mov edi,vocale
        repeta2:
            scasb
            je afara
            loop repeta2
        afara:
        cmp ecx,0
        je gasit
        inc ebx
    gasit:
        jmp repeta
    final:    
        push ebx
        push dword output_format
        call [printf]
        add esp,4*2
        
        push dword [file_descriptor]
        call [fclose]
        add esp,4
            
    eroare:
        push dword 0
        call [exit]