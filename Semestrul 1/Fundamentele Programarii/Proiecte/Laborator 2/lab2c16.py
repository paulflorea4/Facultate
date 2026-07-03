def perfect(x):
    s=0
    i=1
    while i<x:
        if x%i==0:
            s=s+i
        i=i+1
    if s==x:
        return True
    else:
        return False
n=int(input("n="))
n=n-1
while n>=6:
    if perfect(n)==True:
        print(n)
        break
    n=n-1
    
