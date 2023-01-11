from string import ascii_uppercase as AA

dechiffrer = "OQFFQ CGQEFUAZ QEF FDQE RMOUXQ, XM CGQEFUAZ EGUHMZFQ QEF BXGE PURRUOUXQ"

def analyse(sentence):
    stats = {c:sentence.count(c) for c in set(sentence.replace(" ", ""))}
    ll = list(stats.items()) # items() retourne un itérateur en python 3
    ll.sort(key=lambda x:x[1],reverse=True)
    return ll

def dechiffreBrutForce(sentence):
    print("===FORCE BRUT===")
    e = ord('E')
    for shift in range(1,26):
        CC=AA[shift:]+AA[:shift]
        vv = str.maketrans(CC,AA)
        print(sentence.translate(vv) ," shift : ", shift)

def dechiffreAnalyse(sentence, dicOccurence):
    print("===ANALYSE===")
    e = ord('E')
    shift = ord(dicOccurence[0][0]) - e
    CC=AA[shift:]+AA[:shift]
    vv = str.maketrans(CC,AA)
    print(sentence.translate(vv) ," shift : ", shift)


dechiffreBrutForce(dechiffrer)
dechiffreAnalyse(dechiffrer, analyse(dechiffrer))
print("Histogramme :",analyse(dechiffrer))