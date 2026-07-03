bits 32

global start

extern exit,fopen,fclose,scanf,fprintf,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import scanf msvcrt.dll
import fprintf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    max equ 100
    print_message db "Dati un mesaj ascuns:",0
    hidden_message times max db 0
    format db "%s",0
    file_name db "output.txt",0
    mod_acces db "w",0
    descriptor_file dd -1
    message times max/2 db 0
segment code use32 class=code
    start:
        push dword mod_acces
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
        
        push dword print_message
        call [printf]
        add esp,4
        
        push dword hidden_message
        push dword format
        call [scanf]
        add esp,4*2
        
        mov ebx,0
        mov esi,hidden_message
        cld
        mov edi,message
    parcurge_sir:
        lodsb
        cmp al,0
        je sfarsit
        sub al,'0'
        mov bl,16
        mul bl
        mov bx,ax
        lodsb
        cmp al,'A'
        jl cifra
        sub al,'A'
        add al,10
        jmp litera
    cifra:
        sub al,'0'
    litera:
        add al,bl
        stosb
        jmp parcurge_sir
        
    sfarsit:
        
        mov al,[message+1]
        sub edi,message
        dec edi
        mov bl,[message+edi]
        mov [message+1],bl
        mov [message+edi],al
        
        push dword message
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