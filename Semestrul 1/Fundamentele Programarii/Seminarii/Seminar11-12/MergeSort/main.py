from entities import Product


def merge(lst1, lst2, key):
    i = 0
    j = 0
    result = []
    while i < len(lst1) and j < len(lst2):
        if key(lst1[i]) < key(lst2[j]):
            result.append(lst1[i])
            i += 1
        else:
            result.append(lst2[j])
            j += 1

    result.extend(lst1[i:])
    result.extend(lst2[j:])
    return result


def merge_sort_with_key(lst, key=lambda x: x):
    if len(lst) == 1:
        return lst
    middle = len(lst) // 2
    left = lst[:middle]
    right = lst[middle:]
    sorted_left = merge_sort_with_key(left, key)
    sorted_right = merge_sort_with_key(right, key)
    return merge(sorted_left, sorted_right, key)


l = [4, 5, 1, 2, 10, 10, 4, 5, 5, 5, 5]
print(merge_sort_with_key(l))

p1 = Product(1, "ciocolata", 10)
p2 = Product(2, "lapte", 4)
p3 = Product(3, "alune", 16)
p4 = Product(4, "oua", 12)

l2 = [p1, p2, p3, p4]
sorted_l2 = merge_sort_with_key(l2, key=lambda produs: produs.getName())
#This should print sorted by name
for element in sorted_l2:
    print(element)
print("---" * 20)
#This should print sorted by price
sorted_l3 = merge_sort_with_key(l2, key=lambda produs: produs.getPrice())
for element in sorted_l3:
    print(element)


def test_merge_sort_with_key():
    p1 = Product('SWEETS029', 'ciocolata Milka', 7.23)
    p2 = Product('SWEETS437', 'ferrero Rocher', 13.98)
    p3 = Product('SWEETS430', 'turta dulce', 3.88)
    p4 = Product('FRUITS274', 'portocale', 5.99)
    p5 = Product('FRUITS98', 'mandarine', 6.23)
    p6 = Product('FRUITS97', 'mandarine', 3.23)

    products = [p1, p2, p3, p4, p5]
    sorted_products = merge_sort_with_key(products, lambda x: x.getName())

    assert (sorted_products[0] == p1)
    assert (sorted_products[1] == p2)
    assert (sorted_products[2] == p5)
    assert (sorted_products[3] == p4)
    assert (sorted_products[4] == p3)

    sorted_products = merge_sort_with_key(products, lambda x: x.getPrice())

    assert (sorted_products[0] == p3)
    assert (sorted_products[1] == p4)
    assert (sorted_products[2] == p5)
    assert (sorted_products[3] == p1)
    assert (sorted_products[4] == p2)

    products = [p1, p2, p3, p4, p5, p6]
    sorted_products = merge_sort_with_key(products, lambda x: (x.getName(), x.getPrice()))
    assert (sorted_products[0] == p1)
    assert (sorted_products[1] == p2)
    assert (sorted_products[2] == p6)
    assert (sorted_products[3] == p5)
    assert (sorted_products[4] == p4)
    assert (sorted_products[5] == p3)


test_merge_sort_with_key()
