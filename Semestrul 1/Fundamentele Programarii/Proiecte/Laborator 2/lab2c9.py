n=int(input("n="))
palindrom=0
while n!=0:
    palindrom=palindrom*10+n%10
    n=n//10
print(palindrom)
