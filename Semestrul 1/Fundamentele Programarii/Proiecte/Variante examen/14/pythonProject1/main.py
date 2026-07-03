def f(a):
    if a == 2:
        raise ValueError()
    a = a + 1

def main():
    a = 1
    f(a)
    print(a)

if __name__ == '__main__':
    main()