bits 32

global start

extern exit,scanf,fopen,fclose,fprintf,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    cifre times 10 db 0
    file_name db "result.txt",0
    mod_acces db "w",0
    descriptor_file dd -1
    format db "%s",0
    message db "Dati o cifra:",0
    n dd 0
    rez times 5 db 0
segment code use32 class=code
    start:
        push dword mod_acces
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je eroare
        
        cld
    repeta:
        push dword n
        push dword format
        call [scanf]
        add esp,4*2
        
        mov eax,[n]
        cmp eax,'!'
        je sfarsit
        sub eax,'0'
        mov byte [cifre+eax],1
        jmp repeta
        
    sfarsit:
    
        mov esi,cifre+9
        mov edi,rez
        std
        mov ebx,9
    formare_rezultat:
        lodsb
        dec esi
        cmp al,0
        je urmatoarea_cifra
        mov al,bl
        add al,'0'
        cld
        stosb
        std
    urmatoarea_cifra:
        sub ebx,2
        cmp ebx,0
        jl afisare
        jmp formare_rezultat
        
    afisare:
        cld
        push dword rez
        push dword format
        push dword [descriptor_file]
        call [fprintf]
        add esp,4*3
        
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    eroare:
        push dword 0
        call [exit]