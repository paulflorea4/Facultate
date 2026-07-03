bits 32

global start

extern exit,fopen,fclose,printf,fread
import exit msvcrt.dll
import printf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll
;19.Se dau in segmentul de date un nume de fisier si un text (poate contine orice tip de caracter). Sa se calculeze suma cifrelor din text. Sa se creeze un fisier cu numele dat si sa se scrie suma obtinuta in fisier.
segment data use32 class=data
    file_name db "fisier.txt",0
    mod_access db "r",0
    descriptor_file dd -1
    max equ 100
    text times max db 0
    output_format db "Suma cifrelor este %d",0
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
        cld
        mov ebx,0
    repeta:
        xor eax,eax
        lodsb
        cmp al,0
        je afara
        cmp al,'0'
        jb next
        cmp al,'9'
        ja next
        sub al,'0'
        add ebx,eax
    next:
        jmp repeta
    afara:
        push ebx
        push dword output_format
        call [printf]
        add esp,4*3
        
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]