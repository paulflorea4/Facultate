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
n=n-1
gasit=0
while n>=0:
    if prim(n)==True:
        print(n)
        gasit=1
        break
    n=n-1
if gasit==0:
    print("Nu exista")
