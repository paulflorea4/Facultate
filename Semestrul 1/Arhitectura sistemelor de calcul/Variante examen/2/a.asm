bits 32

global start
extern cerinta
extern exit
import exit msvcrt.dll

segment data use32 class=data
    octeti db 00h,79h,13h,61h,0cdh,26h
    len equ $-octeti
segment code use32 class=code
    start:
        push dword octeti
        push dword len
        call cerinta
        add esp,4*2
        push dword 0
        call [exit]