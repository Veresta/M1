def leftrotate(x, c):
    x &= 0xffffffff
    return (x<<c) | (x>>(32-c)) & 0xffffffff