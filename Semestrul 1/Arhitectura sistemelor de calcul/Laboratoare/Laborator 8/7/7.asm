bits 32

global start

extern exit,fopen,fread,fclose,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import fclose msvcrt.dll
import printf msvcrt.dll

;7.Se da un fisier text. Sa se citeasca continutul fisierului, sa se determine litera mica (lowercase) cu cea mai mare frecventa si sa se afiseze acea litera, impreuna cu frecventa acesteia. Numele fisierului text este definit in segmentul de date.
segment data use32 class=data
    file_name db "input.txt",0
    mod_access db "r",0
    file_descriptor dd -1
    frecventa_litere times 26 db 0
    max equ 100
    text times max db 0
    output_format db "%c:%d",0
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
    repeta:
        xor eax,eax
        lodsb
        cmp al,0
        je final
        cmp al,'a'
        jb next
        cmp al,'z'
        ja next
        sub al,'a'
        add byte [eax+frecventa_litere],1
    next:
        jmp repeta
    final: 
    
        mov ebx,0
        mov ecx,26
        mov edx,0
        mov esi,frecventa_litere
        cld
    repeta2:
        lodsb 
        cmp al,bl
        jb mai_mic
        mov bl,al
        mov dl,ah
    mai_mic:
        inc ah
        loop repeta2
        
        add edx,'a'
        push ebx
        push edx
        push dword output_format
        call [printf]
        add esp,4*3
        
        push dword [file_descriptor]
        call [fclose]
        add esp,4
    eroare:
        push dword 0
        call [exit]