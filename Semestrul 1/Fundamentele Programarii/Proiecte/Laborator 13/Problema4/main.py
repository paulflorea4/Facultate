def valid(l:list):
    for i in range(len(l)-1):
        if l[i] >= l[i+1]:
            return False
    return True

def backtracking(result,start,numbers):
    for i in range(start,len(numbers)):
        result.append(numbers[i])
        if valid(result):
            print(result)
            backtracking(result,i+1,numbers)
        result.pop()

def backtracking_iterative(numbers):
    n=len(numbers)
    stack=[(0,[])]
    while stack:
        index,solution=stack.pop()
        if len(solution)>0:
            print(solution)
        for i in range(index,n):
            if not solution or numbers[i]>solution[-1]:
                stack.append((i+1,solution + [numbers[i]]))

def main():
    """
    Spatiu de cautare:Toate sub-secventele posibile ale listei
    Candidat:O sub-secventa nenula cu elementele pastrate in ordinea in care apar in lista intiala
    Conditie de validare:Verificarea daca elementele unui candidat se afla in ordine strict crescatoare
    """
    text_file=open("numbers.txt","r")
    lines=text_file.readlines()
    numbers=[]
    for line in lines:
        line=line.strip()
        if line:
            line_numbers=line.split(",")
            for number in line_numbers:
                numbers.append(int(number))
    print("Varianta recursiva")
    backtracking([],0,numbers)

    print("\nVarianta iterativa")
    backtracking_iterative(numbers)

main()
