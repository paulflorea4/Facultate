bits 32

global start

extern exit,fopen,fclose,fscanf,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fscanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "input.txt",0
    mod_access db "r",0
    descriptor_file dd -1
    input_format db "%x",0
    output_format db "%x - %d",13,10,0
    format db "%d ",0
    n dd 0
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
        push dword n
        push dword input_format
        push dword [descriptor_file]
        call [fscanf]
        add esp,4*3
        
        cmp eax,' '
        je e_spatiu
        cmp eax,-1
        je afara
        
        mov ecx,8
        mov ebx,[n]
        mov edx,0
        
        repeta2:
            shl bl,1
            adc edx,0
            loop repeta2
            
        push edx
        push dword [n]
        push dword output_format
        call [printf]
        add esp,4*3
    e_spatiu:
        jmp repeta
        
    afara:
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]