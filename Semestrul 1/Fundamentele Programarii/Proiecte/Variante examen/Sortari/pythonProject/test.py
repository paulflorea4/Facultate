def selectionSort(l):
    for i in range(0,len(l)-1):
        for j in range(i+1,len(l)):
            if l[i]>l[j]:
                l[i],l[j]=l[j],l[i]

def bubbleSort(l):
    sorted=False
    while not sorted:
        sorted=True
        for i in range(0,len(l)-1):
            if l[i]>l[i+1]:
                l[i],l[i+1]=l[i+1],l[i]
                sorted=False

def insertionSort(l):
    for i in range(1,len(l)):
        ind=i-1
        a=l[i]
        while ind>=0 and a<l[ind]:
            l[ind+1]=l[ind]
            ind-=1
        l[ind+1]=a

def partitionare(l,st,dr):
    poz=st
    for i in range(st,dr):
        if l[i]<l[dr]:
            l[i],l[poz]=l[poz],l[i]
            poz+=1
    l[dr],l[poz]=l[poz],l[dr]
    return poz

def quickSort(l,st,dr):
    if st>=dr:
        return l
    poz=partitionare(l,st,dr)
    quickSort(l,st,poz-1)
    quickSort(l,poz+1,dr)

def mergeSort(l,start,end):
    if start>=end-1:
        return
    m=(start+end)//2
    mergeSort(l,start,m)
    mergeSort(l,m,end)

    left=l[start:m]
    right=l[m:end]
    i=0
    j=0
    k=start
    while i<len(left) and j<len(right):
        if left[i]<right[j]:
            l[k]=left[i]
            i+=1
        else:
            l[k]=right[j]
            j+=1
        k+=1

    while i<len(left):
        l[k] = left[i]
        i += 1
        k+=1

    while j<len(right):
        l[k] = right[j]
        j += 1
        k += 1

def cautare_binara_iterativ(l,el):
    st=0
    dr=len(l)-1
    while st<=dr:
        m=(st+dr)//2
        if el==l[m]:
            return m
        if el<l[m]:
            dr=m-1
        if el>l[m]:
            st=m+1
    return -1

def cautare_binara_recursiv(l,el,st,dr):
    if st>dr:
        return -1
    m=(st+dr)//2
    if el==l[m]:
        return m
    if el<l[m]:
        return cautare_binara_recursiv(l,el,st,m-1)
    else:
        return cautare_binara_recursiv(l,el,m+1,dr)

def secv_cresc(l):
    n=len(l)
    r=[0]*n
    p=[-1]*n
    if l[-1]%2==0:
        r[-1]=1
    for i in range(n-2,-1,-1):
        pmax=i
        if l[i]%2==0:
            for j in range(i+1,n):
                if l[j]%2==0 and l[j]<l[i] and r[j]>r[pmax]:
                    pmax=j
                    p[i]=j
            r[i]=1+r[pmax]

    pmax=0
    for i in range(1,n):
        if r[i]>r[pmax]:
            pmax=i

    rez=[]
    while pmax!=-1:
        rez.append(l[pmax])
        pmax=p[pmax]

    print(rez)

def main():
    l=[int(x) for x in input().split()]
    #selectionSort(l)
    #bubbleSort(l)
    #insertionSort(l)
    #quickSort(l,0,len(l)-1)
    #mergeSort(l,0,len(l))
    #el=int(input())
    #print(cautare_binara_iterativ(l,el))
    #print(cautare_binara_recursiv(l,el,0,len(l)-1))
    secv_cresc(l)

main()