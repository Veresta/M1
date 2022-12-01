import math

def gcd(a, b):
    a,b = abs(a), abs(b)
    r = a%b
    while r:
        a,b=b,r
        r = a%b
    return b

def xgcd(a, b):
    if b == 0: return (a, 1, 0)
    else:
        (d, u, v) = xgcd(b , a%b)
        return (d, v, u - (a//b)* v)


def inversemod(a, n):
    d,u,v = xgcd(a,n)
    if d!=1: raise Exception('Element non inversible')
    return u if u>0 else n+u


def primes(n):
    P = []
    M = [x for x in range(3, n)]
    tmp_0 = 2
    while M != [] :
        M  = [x for x in M if x % tmp_0 != 0]
        P.append(tmp_0)
        if M == []: break
        tmp_0 = M[0]
    return P

def is_prime(n):
    for i in range(2,int(math.sqrt(n))+1):
        if(n%i) == 0: return True
    return False

print("gcd function : ", gcd(192, -36))
print("xgcd function : ", xgcd(36, 196))
print("inversemod function : ", inversemod(5,12))
print("primes function : ", primes(24))
print("is_prime function :", is_prime(4))

def LotOfPrime():
    return [i for i in range(200000) if is_prime(i)]

print(LotOfPrime())

def powermod(a,m,n):
    res = 1
    while m:
        if m & 1: res = (res*a) %n
        m >>= 1
        a = (a*a) %n
    return res

print("powermod function :",powermod(5,666,2**101-1))