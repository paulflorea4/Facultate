bits 32

global start

extern exit,fopen,fprintf,fclose
import exit msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll

segment data use32 class=data
    text db "Ana are multe mere si pere in bucataria mamei sale",0
    file_name db "output.txt",0
    mod_access db "w",0
    file_descriptor dd -1
segment code use32 class=code
    start:
        push dword mod_access
        push dword file_name
        call [fopen]
        
        mov [file_descriptor],eax
        cmp eax,0
        je final
        push dword text
        push dword [file_descriptor]
        call [fprintf]
        add esp,4*2
        
        push dword [file_descriptor]
        call [fclose]
        add esp,4*1
    final:
        push dword 0
        call [exit]