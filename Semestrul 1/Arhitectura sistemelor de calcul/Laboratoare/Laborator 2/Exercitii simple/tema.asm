bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...

; our code starts here
segment code use32 class=code
    start:
		;1. 1+9
		mov AL,1
		add AL,9
		
		;2. 1+15
		mov Al,1
		add Al,15
		
		;3. 128+128
		mov AL,128
		add AL,128
		
		;4. 5-6
		mov BL,5
		sub BL,6
		
		;5. 10/4
		mov AX,10
		mov BL,4
        div BL
		
		;6. 256*1
		mov AX,256
        mov BL,1
		mul BL
        
		;9. 3*4
		mov AL,3
        mov BL,4
		mul BL
		
		;14. -2*5
		mov AL,-2
        mov BL,5
		mul BL
		
		;19. 12/4
		mov AX,12
        mov BL,4
		div BL
		
		;20. 13/3
		mov AX,13
        mov BL,3
		div BL
		
		
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
