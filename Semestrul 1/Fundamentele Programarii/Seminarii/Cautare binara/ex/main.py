def binary_search(el,l,left,right):
    if left>=right-1:
        return right
    mid=(left+right)//2
    if el<=l[mid]:
        return binary_search(el,l,left,mid)
    else:
        return binary_search(el,l,mid+1,right)

def searchBinaryRec(el,l):
    if len(l)==0:
        return 0
    if el<l[0]:
        return 0
    if el>l[len(l)-1]:
        return len(l)
    return binary_search(el,l,0,len(l))

lst=[1,2,3,6,7,9,10,13,16]
print(searchBinaryRec(0, lst))