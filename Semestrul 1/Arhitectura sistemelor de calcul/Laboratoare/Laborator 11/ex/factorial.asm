%ifndef _FACTORIAL_ASM_ ; continuăm dacă _FACTORIAL_ASM_ este nedefinit
%define _FACTORIAL_ASM_ ; și ne asigurăm că devine definit
                        ; astfel, %include permite doar o singură includere

;definire procedura
factorial: ; int _stdcall factorial(int n)
	mov eax, 1
	mov ecx, [esp + 4]
	
	repet: 
		mul ecx
	loop repet ; atentie, cazul ecx = 0 nu e tratat!

	ret 4 ; in acest caz 4 reprezinta numarul de octeti ce trebuie eliberati de pe stiva (parametrul pasat procedurii)

%endif  