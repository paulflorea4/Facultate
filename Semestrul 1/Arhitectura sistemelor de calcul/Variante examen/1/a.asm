bits 32

global start
extern formare_sir       

extern exit,printf               
import exit msvcrt.dll
import printf msvcrt.dll
                        

segment data use32 class=data
    s dd -1,123456,0xabcdeff,0xabcdeff,0xcbcdeff,0xdbcdeff,0111010101b
    len equ ($-s)/4
    sume times len db 0
    index dd 0
    format db "%x ",0
    spatiu db 13,10,0
segment code use32 class=code
    start:
        push dword len
        push dword sume
        push dword s
        call formare_sir
        add esp,4*3
        
        mov ecx,len-1
        mov esi,sume+1
        mov bl,[sume]
        cld
        mov dl,1
    repeta:
        lodsb
        cmp bl,al
        jb urmator
        cmp dl,1
        je nu_e_secv
        push esi
        mov esi,[index]
        lea esi,[4*esi]
        add esi,s
        push eax
        push ebx
        push ecx
        push edx
        movzx ecx,dl
        repeta2:
            lodsd
            push ecx
            push eax
            push dword format
            call [printf]
            add esp,4*2
            pop ecx
            loop repeta2
        push dword spatiu
        call [printf]
        add esp,4
        pop edx
        pop ecx
        pop ebx
        pop eax
        pop esi
        mov dl,0
        mov dword [index],len
        sub [index],ecx
    urmator:
        inc dl
        jmp urmator2
    nu_e_secv:
        mov dword [index],len
        sub [index],ecx
    urmator2:
        mov bl,al
        loop repeta
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
