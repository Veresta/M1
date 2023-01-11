from string import ascii_uppercase as AA

frenchFreq = "ESAURINOTLPCMDBVGHFJQZXYW"

def analyse(sentence):
    stats = {c:sentence.count(c) for c in set(sentence.replace(" ", ""))}
    ll = list(stats.items()) # items() retourne un itérateur en python 3
    ll.sort(key=lambda x:x[1],reverse=True)
    return ll

with open("mono.txt") as file:
    data = file.read()
    analys = analyse(data)
    combinedAnalys = ''.join([analys[x][0] for x in range(0,39) if analys[x][0] in AA])
    ww=str.maketrans(combinedAnalys,frenchFreq)
    print (data.translate(ww))
    