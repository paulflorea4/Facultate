bits 32

global cerinta
extern printf
import printf msvcrt.dll

segment data use32 class=data
    lungime dd 0
    cuvant_curent resw 1
    format_afisare db "%xh ",0
    d db 11
segment code use32 class=code
    cerinta:
        mov ecx,[esp+4]
        mov [lungime],ecx
        mov edi,[esp+8]         
        mov esi,edi
        mov ebx,0
        cld
        repeta1:
            lodsb
            mov [cuvant_curent+1],al
            
            mov edx,0
            push ecx
            push esi
            mov esi,edi
            mov ecx,[lungime]
            
            repeta2:
                mov ah,[cuvant_curent+1]
                lodsb
                cmp ebx,edx             ;comparam sa nu fie acelasi octet
                je egale
                
                mov [cuvant_curent],al
                and ax,1                ;verificare impar
                jz egale    
                
                mov ax,[cuvant_curent]
                div byte [d]            ;verificare divizibil cu 11
                cmp ah,0
                jne egale
                
                pushad                  ;afisare
                mov eax,0
                mov ax,[cuvant_curent]
                push eax
                push dword format_afisare
                call [printf]
                add esp,4*2
                popad
            egale:
                inc edx
            loop repeta2  
            
            pop esi
            pop ecx
            inc ebx
        loop repeta1
        
        ret 