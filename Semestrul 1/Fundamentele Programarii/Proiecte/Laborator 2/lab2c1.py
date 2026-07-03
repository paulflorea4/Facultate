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
    
def f(n):
    a=n+1
    while True:
        if prim(a)==True:
            return a
        a=a+1
        
n=int(input())
print(f(n))
