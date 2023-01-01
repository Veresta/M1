import re


def verifyexercice1():
    print(eval('*'.join(map(str, range(1, 5)))))
    print(2 ^ 2 or repr(42))

    x = ('t' + "o") * 2
    d = {'y': x}
    print(x.replace("oto", "urlu") + d['y'].replace("o", 'u'))
    print("    \t  x yz     \n".strip())
    print(''.join(map(lambda g: str(sum(map(int, str(g)))), range(22, 19, -2))))


# Exercice 3
def treemap(f, t):
    aux = []
    for tree in t:
        if type(tree) == type(0):
            aux.append(f(tree))
        elif type([]) not in map(lambda x: type(x), tree):
            aux.append(list(map(f, tree)))
        else:
            aux.append(treemap(f, tree))
    return aux

# Exercice 4
def vectorize(f):
    def applyf(*args):
        return list(map(f, *args))
    return applyf

@vectorize
def f(x):
    return x * x

#Exercice 5
def make_slug(s):
    return '-'.join(re.findall(r"\w+", s))

#Exercice 6
def flatten(lst):
    aux = []
    for e in lst:
        if type(e) != type([]):
            aux.append(e)
        else:
            aux += flatten(e)
    return aux

def main():
    print("Exercice 1")
    verifyexercice1()
    print("Exercice 2 (Pas encore fait)")
    print("Exercice 3")
    print(treemap(lambda x: x * x, [[0, 1], [[[2, 3], [4, 5]]], [[6, 7, [8, 9]]]]))
    print("Exercice 4")
    print(f([1, 2, 3, 4]))
    print("Exercice 5")
    print(make_slug("    ga bu  ** -- zo  meu ! !  666"))
    print("Exercice 6")
    print(flatten([[1, [2, 3], 4], [[[5, [6, 7]]], [8, 9]]]))

if __name__ == '__main__':
    main()
