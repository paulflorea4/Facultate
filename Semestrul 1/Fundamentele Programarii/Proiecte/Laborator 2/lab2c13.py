def prim(x):
    div=0
    i=1
    while i<=x:
        if x%i==0:
            div=div+1
        i=i+1
    if div==2:
        return True
    else:
        return False

n=int(input("n="))
i=1
if n==1:
    print(n)
else:
    gasit=0
    n=n-1
    while gasit==0:
        if prim(i)==True:
            n=n-1
            if n==0:
                print(i)
                gasit=1
        else:
            j=2
            while j<i:
                if prim(j)==True and i%j==0:
                    n=n-j
                    if n<=0:
                        print(j)
                        gasit=1
                        break
                j=j+1
        i=i+1
