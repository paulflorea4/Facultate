nasm -fobj a.asm
nasm -fobj b.asm
alink a.obj b.obj -oPE -subsys  console -entry start