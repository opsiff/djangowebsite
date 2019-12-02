from django.shortcuts import render
from django.http import HttpResponse, HttpResponseNotFound,JsonResponse
from django.views.decorators.http import require_http_methods


from .models import User,OnlineUser
import json
from .utils import jwcAuth
from .utils import Token
# Create your views here.



@require_http_methods(["GET", "POST"])
def loginIn(request):
    result={
        "status": 500,
        "message":None,
        "data":None
    }
    if request.method == 'GET':
        msg="请使用post请求"
        result["message"]=msg
        return JsonResponse(result,safe=False,status=200)
    elif request.method == 'POST':
        try:
            userAccount = request.POST["muser"]
            passWord= request.POST["passwd"]
            dic={}
            dic["muser"]=userAccount
            dic["passwd"]=passWord
            dic=jwcAuth.request(dic)
            if dic==-1:
                raise Exception
            #检查登录
            res = User.objects.filter(account=userAccount)
            if len(res)==0:
                print("为用户{0}创建账户".format(userAccount))
                User.objects.create(account=userAccount, passwd=passWord)
            else:
                print("用户{0}登录成功".format(userAccount))
            #生成/修改token
            res = OnlineUser.objects.filter(account=userAccount)
            if len(res) == 0:
                print("为用户{0}创建token".format(userAccount))
                OnlineUser.objects.create(account=userAccount,token=Token.get_token(str(userAccount)))
            else:
                print("为用户{0}修改token".format(userAccount))
                res[0].token=Token.get_token(str(userAccount))
            result["status"]=200
            result["message"]="Success"
            result["data"]=dic
            return JsonResponse(result,safe=False,status=200)
        except Exception as e:
            result["message"]=str(e)
            return JsonResponse(result,safe=False,status=200)

@require_http_methods(["GET", "POST"])
def loginOut(request):
    result = {
        "status": 500,
        "message": None,
        "data": []
    }
    if request.method=="POST":
        userAccount = request.POST["muser"]
        passWord = request.POST["passwd"]
        res = OnlineUser.objects.filter(account=userAccount)
        if len(res) == 0:
            print("用户{0}没有在线".format(userAccount))
            result["message"]='Failed to delete token'
            # OnlineUser.objects.create(account=userAccount, token=Token.get_token(str(userAccount)))
        else:
            print("为用户{0}删除token".format(userAccount))
            result["message"] = 'Success'
            res[0].delete()
    return JsonResponse(result,safe=False,status=200)


# def check(request):