n=int(input("n="))
if n==0:
    print(0)
else:
    p=1
    i=2
    while n!=1:
        if n%i==0:
            p=p*i
            while n%i==0:
                n=n//i
        i=i+1
    print(p)
            
            
