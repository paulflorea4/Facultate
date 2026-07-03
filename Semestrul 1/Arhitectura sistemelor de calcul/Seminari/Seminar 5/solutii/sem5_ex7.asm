bits 32
global start

extern exit,fopen,fread,fclose,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import fclose msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "words.txt",0
    mod_access db "r",0
    file_descriptor dd 0
    
    max equ 100
    buffer times max db 0
    len dd 0
    output_format db "Numarul de cuvinte este %d",0
   
; 7. Se dă un nume de fișier (definit în segmentul de date).
; Fișierul conține cuvinte (șiruri de caractere separate prin spații).
; Să se determine și să se afișeze numărul de cuvinte din fișier.
segment code use32 class=code
    start:
        ; deschid fisierul
        ; EAX = fopen(nume_fisier, mod_acces)
        push dword mod_access
        push dword file_name
        call [fopen]
        add esp,4*2
        
        cmp eax,0
        je file_error
        mov [file_descriptor],eax
        
        ; citesc continutul intregului fisier
        ; EAX = fread(void* buffer, int size, int count, FILE* stream)
        push dword [file_descriptor]
        push dword max
        push dword 1
        push buffer
        call [fread]
        add esp,4*4
        
        mov ebx,0
        mov esi,buffer
        mov edx,0
    loop1:
        lodsb
        cmp al,0
        je last_word
        cmp al,' '
        jne is_word
        cmp edx,0
        je next
        inc ebx
        mov edx,0
        jmp next
    is_word:
        mov edx,1
    next:
        jmp loop1

    last_word:
        cmp edx,1
        jne write
        inc ebx
    write:
        mov [len],ebx
        push dword [len]
        push dword output_format
        call [printf]
        add esp,4*2
        
        push dword [file_descriptor]
        call [fclose]
        add esp,4*2
        
    file_error:
        push dword 0
        call [exit]
