bits 32

global start

extern exit,fopen,fclose,fscanf,fprintf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fscanf msvcrt.dll
import fprintf msvcrt.dll

segment data use32 class=data
    input_file db "input.txt",0
    output_file db "output-i.txt",0
    descriptor_input_file dd -1
    descriptor_output_file dd -1
    mod_acces_input db "r",0
    mod_acces_output db "w",0
    string db "abcdefgh",0
    format_input db "%d",0
    format_output db "%c ",0
    n dd 0
segment code use32 class=code
    start:
        push dword mod_acces_input
        push dword input_file
        call [fopen]
        add esp,4*2
        
        mov [descriptor_input_file],eax
        cmp eax,0
        je final
        
        push dword n
        push dword format_input
        push dword [descriptor_input_file]
        call [fscanf]
        add esp,4*3
        
        push dword [descriptor_input_file]
        call [fclose]
        add esp,4
        
        mov ecx,[n]
        inc ecx
    repeta:
        push ecx
        mov bl,cl
        add bl,'0'
        mov [output_file+7],bl
        push dword mod_acces_output
        push dword output_file
        call [fopen]
        add esp,4*2
        
        mov [descriptor_output_file],eax
        cmp eax,0
        je next
        
        pop ecx
        push ecx
        mov esi,string+7
        repeta2:
            std
            push ecx
            xor eax,eax
            lodsb
            cld
            push dword eax
            push dword format_output
            push dword [descriptor_output_file]
            call [fprintf]
            add esp,4*3
            pop ecx
            loop repeta2 
        push dword [descriptor_output_file]
        call [fclose]
        add esp,4
    next:
        pop ecx
        loop repeta

    final:
        push dword 0
        call [exit]