%ifndef _CONCATENARE_ASM_
%define _CONCATENARE_ASM_

concatenare:
        mov esi,[esp+4]
        mov edi,[esp+12]
        
        repeta:
            lodsb
            cmp al,'0'
            jge si1
            jmp continua
        si1:
            cmp al,'9'
            jg continua
                stosb
            continua:
            cmp al,0
            je afara
            jmp repeta
            
        afara:
        
        mov esi,[esp+8]
        
        repeta2:
            lodsb
            cmp al,'0'
            jge si2
            jmp continua2
        si2:
            cmp al,'9'
            jg continua2
                stosb
            continua2:
            cmp al,0
            je afara2
            jmp repeta2
            
        afara2:
        
        ret
        
%endif 