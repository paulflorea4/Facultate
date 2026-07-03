def perfect(x):
    i=1
    s=0
    while i<x:
        if x%i==0:
            s=s+i
        i=i+1
    if x==s:
        return True
    else:
        return False

n=int(input("n="))
n=n+1
gasit=0
while gasit==0:
    if perfect(n)==True:
        print(n)
        break
    n=n+1
