bits 32 

global start

extern exit,fopen,fclose,scanf,fread,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import scanf msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "cuvinte.txt",0
    mod_acces db "r",0
    descriptor_file dd -1
    L resd 1
    message db "Dati o valoare:",0
    input_format db "%d",0
    output_format db "Numarul de cuvinte:%d",0
    max equ 100
    text times max db 0
segment code use32 class=code
    start:
        push dword mod_acces
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
        
        push dword message
        call [printf]
        add esp,4
        
        push dword L
        push dword input_format
        call [scanf]
        add esp,4*2
        
        mov esi,text
        cld
        mov ebx,0
        mov edx,0
    repeta:
        lodsb
        cmp al,0
        je sfarsit
        cmp al,' '
        je e_spatiu
        inc ebx
        jmp next
    e_spatiu:
        test ebx,1
        jz par
        cmp ebx,[L]
        jae par
        inc edx
    par:
        mov ebx,0
    next:
        jmp repeta
    sfarsit:
        test ebx,1
        jz afisare
        cmp ebx,[L]
        jae afisare
        inc edx
    afisare:    
        push edx
        push dword output_format
        call [printf]
        add esp,4*2
        
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]