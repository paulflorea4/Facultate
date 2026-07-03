n1=int(input("n1="))
n2=int(input("n2="))
cifN1=[0,0,0,0,0,0,0,0,0,0]
cifN2=[0,0,0,0,0,0,0,0,0,0]
if n1==0:
    cifN1[0]=cifN1[0]+1
if n2==0:
    cifN2[0]=cifN2[0]+1
while n1!=0:
    cifN1[n1%10]=cifN1[n1%10]+1
    n1=n1//10
while n2!=0:
    cifN2[n2%10]=cifN2[n2%10]+1
    n2=n2//10
ok=1
for i in range(10):
    if (cifN1[i]==0 and cifN2[i]!=0) or (cifN1[i]!=0 and cifN2[i]==0):
        ok=0
if ok==1:
    print("Numerele au proprietatea P")
else:
    print("Numerele nu au proprietatea P")
