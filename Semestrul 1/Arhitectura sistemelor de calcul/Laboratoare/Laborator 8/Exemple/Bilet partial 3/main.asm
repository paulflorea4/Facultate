bits 32

global start

extern exit,fopen,fclose,fprintf,scanf,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "output.txt",0
    access_mode db "w",0
    descriptor_file dd -1
    n resd 1
    output_format db "%d = %x = %s",13,10,0
    input_format db "%d",0
    message db "Dati un numar in baza 10:",0
    biti times 32 db 0
segment code use32 class=code
    start:
        push dword access_mode
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
        push dword input_format
        call [scanf]
        add esp,4*2
        
        mov ebx,[n]
        cmp ebx,0
        je afara
        
        mov edi,biti
        cld
        mov ecx,32
        repeta2:
            xor al,al
            shl ebx,1
            adc al,'0'
            stosb
            loop repeta2
            
        push dword biti
        push dword [n]
        push dword [n]
        push dword output_format
        push dword [descriptor_file]
        call [fprintf]
        add esp,4*4
        
        jmp repeta
    afara:
    
        push dword [descriptor_file]
        call [fclose]
        add esp,4
        
    final:
        push dword 0
        call [exit]