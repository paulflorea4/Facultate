bits 32

global start

extern exit,fopen,fclose,fprintf,scanf,printf
import exit msvcrt.dll
import fclose msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "output.txt",0
    mod_acces db "w",0
    descriptor_file dd -1
    message db "Dati un cuvant:",0
    format db "%s",0
    output_format db "%s ",0
    cuvant resd 1
    
segment code use32 class=code
    start:
        push dword mod_acces
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
    repeta:
        push dword message
        call [printf]
        add esp,4
        
        push dword cuvant
        push dword format
        call [scanf]
        add esp,4*2
        
        cmp byte [cuvant],'$'
        je sfarsit
      
        mov esi,cuvant
        cld
    cauta:
        lodsb
        cmp al,0
        je sfarsit_cuvant
        cmp al,'0'
        jb next
        cmp al,'9'
        ja next
        jmp afisare
        
    next:
        jmp cauta
        
    afisare:    
        push dword cuvant
        push dword output_format
        push dword [descriptor_file]
        call [fprintf]
        add esp,4*3
        
    sfarsit_cuvant:
        jmp repeta
        
    sfarsit:
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]