bits 32

global start

extern exit,fopen,fclose,fprintf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
;14.Se dau un nume de fisier si un text (definite in segmentul de date). Textul contine litere mici, litere mari, cifre si caractere speciale. Sa se transforme toate literele mari din textul dat in litere mici. Sa se creeze un fisier cu numele dat si sa se scrie textul obtinut in fisier.
segment data use32 class=data
    file_name db "output.txt",0
    mod_access db "w",0
    descriptor_file dd -1
    text db "aBcdEF%ghIjKlmno@#",0
    len equ $-text
    format db "%s",0
segment code use32 class=code
    start:
        push dword mod_access
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
        
        mov esi,text
        mov edi,text
        cld 
        mov ecx,len
        jecxz sfarsit
    repeta:
        lodsb
        cmp al,'A'
        jb urmatorul
        cmp al,'Z'
        ja urmatorul
        add al,'a'-'A'
    urmatorul:
        stosb
        loop repeta
    sfarsit:
        push dword text
        push dword format
        push dword [descriptor_file]
        call [fprintf]
        add esp,4*3
        
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]