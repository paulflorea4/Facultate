x=int(input())
div=0
i=1
while i<=x:
    if x%i==0:
        div=div+1
    i=i+1
print("Prim")if div==2 else print("Nu e prim")
    
