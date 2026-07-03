%ifndef _BAZA2_ASM_
%define _BAZA2_ASM_

    base2:
        mov ecx,32
        mov edi,[esp+4]
        repeta:
            xor al,al
            shl edx,1
            adc al,'0'
            stosb
            loop repeta
        
        ret 4


%endif