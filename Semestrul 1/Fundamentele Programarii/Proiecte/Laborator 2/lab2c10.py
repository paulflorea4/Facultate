n=int(input("n="))
m=0
if n==0:
    print(0)
else:
    cifre=[]
    while n!=0:
        cifre.append(n%10)
        n=n//10
    cifre.sort()
    m=0
    for cif in cifre:
        m=m*10+cif
    print(m)
