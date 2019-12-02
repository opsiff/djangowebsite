import requests
import json
import time

form = {
    "muser": "04170324",
    "passwd": "10086KKDDaaggccd"
}
def Login():
    x = requests.post("http://122.112.159.211/authorize/loginIn", data=form)
    print(x.text)

def LoginOut():
    x = requests.post("http://127.0.0.1:8000/authorize/loginOut", data=form)
    print(json.loads(x.text))


def upload():
    try:
        data={
            "price": "11.2",
            "phone":"1232",
            "user":"041702324",
            "detail":"1112"
        }
        files=[("img",open('./2.jpg', 'rb')),("img",open('./1.jpg', 'rb'))]
        x = requests.post("http://127.0.0.1:8000/message/1/submmit",data=data,files=files)
        print(json.loads(x.text, encoding='utf-8'))
    except Exception as e:
        print(e)

if __name__=="__main__":
    # upload()
    Login()
    # LoginOu
# t()
    # print(int(round(time.time() * 1000)))
