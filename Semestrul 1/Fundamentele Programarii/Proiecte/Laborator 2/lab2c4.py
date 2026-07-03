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
p1=2
gasit=0
while gasit==0:
    if  prim(p1)==True:
        p2=n-p1
        if prim(p2)==True:
            gasit=1
        else:
            p1=p1+1
print(f"{n}={p1}+{p2}")
    
