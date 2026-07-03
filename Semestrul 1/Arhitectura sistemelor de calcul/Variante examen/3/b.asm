bits 32

global formare_sir

segment code use32 class=code
    formare_sir:
        mov edi,[esp+4]
        mov esi,[esp+8]
        mov ecx,[esp+12]
        cld
        repeta:
            lodsd
            mov bl,0
            calcul_suma:
                mov edx,eax
                and edx,0fh
                add bl,dl
                shr eax,4
                cmp eax,0
                jne calcul_suma
            mov al,bl
            stosb
        loop repeta
                
        ret