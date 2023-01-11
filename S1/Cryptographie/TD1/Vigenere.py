from string import ascii_uppercase as AA

def stats(sentence):
    return {c:sentence.count(c) for c in set(sentence.replace(" ", ""))}
    

def coincidence(stats):
    size = 0
    coef = 0
    for elem in list(stats.items()):
        if(elem[0].isalpha()): size+= elem[1]
    for letter in AA:
        tmp = stats.get(letter)
        coef += (tmp * (tmp - 1)) / (size * (size - 1))
    return coef

        
with open("poly.txt") as file:
    data = file.read()
    res = stats(data)
    print(coincidence(res))







