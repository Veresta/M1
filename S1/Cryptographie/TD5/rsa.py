from codecs import encode, decode

def xgcd(a, b):
    if b == 0: return (a, 1, 0)
    else:
        (d, u, v) = xgcd(b , a%b)
        return (d, v, u - (a//b)* v)

def inversemod(a, n):
    d,u,v = xgcd(a,n)
    if d!=1: raise Exception('Element non inversible')
    return u if u>0 else n+u

def text2int(t):
    assert len(t) <= 64 # 64*8 = 512 bits
    if isinstance(t,str):
        return int(encode(t.encode(),'hex'), 16) # t.encode() nécessaire en Python 3 pour avoir des bytes
    elif isinstance(t,bytes):
        return int(encode(t,'hex'), 16)
    

def int2text(m):
    s = '%x' % m
    if len(s)%2: s = '0'+s
    return decode(s, 'hex')

class RSA:
    def __init__(self, p, q, e) -> None:
        self._phi_n = (p - 1)*(q - 1)
        self._e = e
        self._n = p * q 
        self._d = inversemod(self._e, self._phi_n)

    def encrypt(self, x):
        return hex(pow(x, self._e, self._n))
    
    def decrypt(self,y):
        c = pow(y, self._d, self._n)
        return c

p = 1113954325148827987925490175477024844070922844843
q = 1917481702524504439375786268230862180696934189293
 

A = RSA(p, q, 3)
encrypt = A.encrypt(1808808319415691415062465989446183136395423154715795462152356725976667671981921260211627443446049)
print("encrypt : ", encrypt)