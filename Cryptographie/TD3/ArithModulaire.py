import math
import random
import sys

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

def fermat_prime_function(p, numberOfTest):
    liste = list()
    for i in range(numberOfTest):
        a = random.randint(1, p-1)
        if(xgcd(a, p) == 1):
            liste.append(a)
    print(liste)
    for a in liste:
        if(powermod(a,p-1, p) != 1):
            print(p, " is not prime")
            return False
    print(p, "is probably prime")
    return True

def fermat_test(p, testNb):
    for j in range(testNb):
        a = random.randint(1,p-1)
        if powermod(a, p-1, p) != 1:
            return False
    return True

print(fermat_test(9,5))

def lotOfFermat(number):
    res = []
    for i in range(2, number):
        if fermat_test(i, 5)==True:
            res.append(i)
    return res

print(lotOfFermat(100))

def miller_rabin(n , num_trials = 4):
    if n % 2 == 0 or n < 2:
        return False
    d = n - 1
    s = 0
    while (d % 2 == 0):
        d //= 2
        s+=1
    for i in range(num_trials):
        a = random.randint(1, n-1)
        x = powermod(a, d, n)
        if x == 1 or x == n - 1:
            continue
    for r in range(1, s):
        x = powermod(x, 2, n)
        if x == 1:
            return False
        if x == n-1:
            break
    
    if x!= n - 1:
        return False 
    
    return True

res = []
for i in range(100):
    if miller_rabin(i) == True:
        res.append(i)
print(res)
