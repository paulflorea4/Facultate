bits 32 

global start        

extern exit,scanf,printf,fopen,fprintf,fclose               
import exit msvcrt.dll    
import scanf msvcrt.dll
import printf msvcrt.dll
import fopen msvcrt.dll
import fprintf msvcrt.dll
import fclose msvcrt.dll
;Se da un nume de fisier (definit in segmentul de date). Sa se creeze un fisier cu numele dat, apoi sa se citeasca de la tastatura numere si sa se scrie valorile citite in fisier pana cand se citeste de la tastatura valoarea 0.
segment data use32 class=data
    file_name db 'output.txt',0
    mod_access db 'w',0
    file_descriptor dd -1
    n dd 0
    input_format db "%d",0
    message db "Dati valori:",0
    error_message db 'Eroare la deschiderea fisierului',0
    output_format db "%d ",0
    values dd 0
    
segment code use32 class=code
    start:
        push dword message
        call [printf]
        add esp,1*4
        
        cld
        mov edi,values
        mov ebx,0
        
    citire:
        push dword n
        push dword input_format
        call [scanf]
        add esp,2*4
        
        mov eax,[n]
        cmp eax,' '
        je e_spatiu
        
        cmp eax,0
        je scriere_in_fisier
        
        stosd
        inc ebx
        
    e_spatiu:
        jmp citire
       
    scriere_in_fisier:
        push dword mod_access
        push dword file_name
        call [fopen]
        add esp,2*4
        
        mov [file_descriptor],eax
        cmp eax,0
        je eroare
        mov esi,values
        mov ecx,ebx 
        repeta:
            push ecx
            lodsd
            push eax
            push dword output_format
            push dword [file_descriptor]
            call [fprintf]
            add esp,3*4
            pop ecx
        loop repeta
        push dword [file_descriptor]
        call [fclose]
        add esp,1*4
        jmp final
    eroare:
        push dword error_message
        call [printf]
        add esp,1*4
    final:
        ; exit(0)
        push    dword 0      
        call    [exit]       
