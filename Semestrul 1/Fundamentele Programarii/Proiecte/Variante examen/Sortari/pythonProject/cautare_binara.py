
def cautare_binara_iterativ(lista,element):
    st=0
    dr=len(lista)-1
    while st<=dr:
        mij=(st+dr)//2
        if lista[mij]==element:
            return mij
        if element<lista[mij]:
            dr=mij-1
        if element>lista[mij]:
            st=mij+1
    return -1

def cautare_binara_recursiv(lista,element,st,dr):
    if st>dr:
        return -1
    mij=(st+dr)//2
    if lista[mij]==element:
        return mij
    if element<lista[mij]:
        return cautare_binara_recursiv(lista,element,st,mij-1)
    else:
        return cautare_binara_recursiv(lista,element,mij+1,dr)

def main():
    l=[int(x) for x in input().split()]
    el=int(input())
    print(cautare_binara_recursiv(l,el,0,len(l)-1))

main()