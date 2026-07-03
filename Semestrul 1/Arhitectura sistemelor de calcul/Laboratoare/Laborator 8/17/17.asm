bits 32

global start

extern exit,fopen,fprintf,fclose,scanf,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll
;17.Se da un nume de fisier (definit in segmentul de date). Sa se creeze un fisier cu numele dat, apoi sa se citeasca de la tastatura numere si sa se scrie din valorile citite in fisier numerele divizibile cu 7, pana cand se citeste de la tastatura valoarea 0.
segment data use32 class=data
    file_name db "output.txt",0
    mod_access db "w",0
    descriptor_file dd -1 
    format db "%d",0
    output_format db "%d ",0
    message db "Dati o valoare:",0
    n resd 1
segment code use32 class=code
    start:
        push dword mod_access
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
        
        push dword n
        push dword format
        call [scanf]
        add esp,4*2
        
        mov eax,[n]
        mov edx,0
        cmp eax,0
        je afara
        mov ebx,7
        div ebx
        cmp edx,0
        jne next
        push dword [n]
        push dword output_format
        push dword [descriptor_file]
        call [fprintf]
        add esp,4*2
    next:
        jmp repeta
    afara:
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]