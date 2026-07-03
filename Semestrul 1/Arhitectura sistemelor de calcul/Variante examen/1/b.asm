bits 32

global formare_sir

segment code use32 class=code
    formare_sir:
        mov ecx,[esp+12]
        mov esi,[esp+4]
        mov edi,[esp+8]
        cld
    repeta:
        mov bl,0
        lodsd
        calculare_suma:
            mov edx,eax
            and edx,0fh
            add bl,dl
            shr eax,4
            cmp eax,0
            jne calculare_suma
        mov al,bl
        stosb
        loop repeta
        ret
