from string import ascii_uppercase as AA

def inverse(k):
    m = [1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25]
    for i in m:
        if((i*k)%26==1):
            return i
    return None

class matrice2D:
    def __init__(self, a, b, c, d):
        self.a = a
        self.b = b
        self.c = c
        self.d = d
    
    def determinant(self):
        return (self.a * self.d) - (self.b * self.c)

    def inverse(self):
        return matrice2D(self.d, -self.b, -self.c, self.d)
    
    def print(self):
        print("(",self.a, "|",self.b,")")
        print("(",self.c, "|",self.d,")")
        

    def mul(self, block):
        return (block[0] * self.matrice[0][0] + block[1] * self.matrice[1][0], block[0] * self.matrice[0][1] + block[1] * self.matrice[1][1])
    
    def matmul(self,matrice):
        pass

    def rmul(self,bloc):
        pass


translate = AA

A = matrice2D([1,3,5,12])
