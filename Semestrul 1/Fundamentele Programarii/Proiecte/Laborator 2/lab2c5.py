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
n=n+1
gasit=0
while gasit==0:
    if prim(n)==True and prim(n+2)==True:
        print(f"{n} {n+2}")
        gasit=1
    n=n+1
        
        
