bits 32

global start

extern exit,fopen,fclose,fread,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    file_name db "input.txt",0
    mod_acces db "r",0
    descriptor_file dd -1
    max equ 100
    text times max db 0
    
    propozitie times max db 0
    contor_propozitie dd 0
    sfarsit_propozitie dd 1
    cuvant_inversat times 50 db 0
    primul_cuvant dd 0
    inceput_cuvant dd 0
    output_format db "Propozitia %d:%s Nr cuvinte: %d, Primul cuvant %s are %d litere",13,10,0
    jmp start
segment code use32 class=code
    inversare: 
        mov ecx,edx
        mov esi,[inceput_cuvant]
        mov edi,cuvant_inversat
        add edi,edx
        dec edi
        inverseaza:
            cld
            lodsb
            std
            stosb
            loop inverseaza
        jmp inapoi
    clear:
        mov ecx,25
        mov edi,propozitie
        repeta3:
            mov eax,0
            stosd
            loop repeta3
        jmp inapoi2
    start:
        push dword mod_acces
        push dword file_name
        call [fopen]
        add esp,4*2
        
        mov [descriptor_file],eax
        cmp eax,0
        je final
        
        push dword [descriptor_file]
        push dword max
        push dword 1
        push dword text
        call [fread]
        add esp,4*4
       
        mov esi,text
        mov edi,propozitie
    repeta:
        cld
        lodsb
        cmp al,0
        je afara
        stosb
        cmp dword [sfarsit_propozitie],1
        je egal
        cmp al,' '
        je e_spatiu
        cmp al,'.'
        je e_sfarsit
        cmp dword [primul_cuvant],1
        jne nu_e_primul
        inc edx
        jmp next
    e_sfarsit:
        mov dword [sfarsit_propozitie],1
        inc ebx
        push edx
        push dword cuvant_inversat
        push ebx
        push dword propozitie
        push dword [contor_propozitie]
        push dword output_format
        call [printf]
        add esp,4*6
        push edi
        jmp clear
        pop edi
    inapoi2:
        mov edi,propozitie
        jmp next
    e_spatiu:
        inc ebx
        cmp dword [primul_cuvant],1
        jne nu_e_primul
        mov dword [primul_cuvant],0
        push edi
        push esi
        jmp inversare
    inapoi:
        pop esi
        pop edi
    nu_e_primul:
        jmp next
    egal:
        mov ebx,0
        add dword [contor_propozitie],1
        mov dword [primul_cuvant],1
        mov dword [sfarsit_propozitie],0
        mov edx,1
        dec esi
        mov [inceput_cuvant],esi
        inc esi
    next:
        jmp repeta
    afara:
        push dword [descriptor_file]
        call [fclose]
        add esp,4
    final:
        push dword 0
        call [exit]
