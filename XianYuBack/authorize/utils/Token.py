import time


def get_token(userNumber):
    t=time.time()
    x=int(round(t*1000000))
    token=str(x)+userNumber
    return token

# 1574842175560572041702324
# 1574842197056093041702324
if __name__=="__main__":
    print(get_token('041702324'))