%ifndef _EX3_MODUL_ASM_
%define _EX3_MODUL_ASM_
    concatenare:
        mov esi,[esp+4]
        mov edi,[esp+12]
    repeta1:
        lodsb
        cmp al,0
        je afara1
        stosb
        jmp repeta1
    afara1:
    
        mov esi,[esp+8]
    repeta2:
        lodsb
        cmp al,0
        je afara2
        stosb
        jmp repeta2
    afara2:
    
        ret
%endif
    
    