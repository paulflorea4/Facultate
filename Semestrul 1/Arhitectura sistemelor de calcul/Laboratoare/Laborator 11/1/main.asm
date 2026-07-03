bits 32

global start

extern exit,scanf,printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    a resd 1
    input_format db "%d",0
    input_message db "Dati un numar reprezentat pe 32 de biti fara semn:",0
    output_format db "Reprezentarea in baza 16 a numarului: %x",0
    output_format2 db "%x",0
    endl db 10,0
segment code use32 class=code
    start:
        push dword input_message
        call [printf]
        add esp,4
    
        push dword a
        push dword input_format
        call [scanf]
        add esp,4*2
        
        push dword [a]
        push dword output_format
        call [printf]
        add esp,4*2
        
        push dword endl
        call [printf]
        add esp,4
        
        mov ecx,32
        mov eax,[a]
    repeta:
        push ecx
        
        ror eax,1   
        mov [a],eax
        
        push eax
        push dword output_format2
        call [printf]
        add esp,4*2
        
        push dword endl
        call [printf]
        add esp,4
        
        mov eax,[a]
        pop ecx
        loop repeta
        
        push dword 0
        call [exit]