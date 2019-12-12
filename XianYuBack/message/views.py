from django.shortcuts import render
import json,time
from utils.common import hashFile
from .models import Message
from django.http import HttpResponse, HttpResponseNotFound,JsonResponse
# Create your views here.
def returnDetail(request,id,category):
    pass


def submmit(request,category): #提交信息 二手信息、任务信息
    result = {
        "status": 500,
        "message": None,
        "data": None
    }
    if request.method=="POST":
        try:
            msgId=str((int(time.perf_counter() * 1000000000000)))
            #msgId=str(int(round(time.time())*100000))
            price=request.POST["price"]
            phone=request.POST["phone"]
            user=request.POST["user"]
            detail=request.POST["detail"]
            imgSrc=request.FILES.get('img')
            try:
                price=request.POST["price"]
                print(price)
            except Exception as e:
                print(e)
            res=Message.objects.filter(msgId=msgId)
            import random
            ranstr=str(random.randint(65536,214748364))
            msgpathname=ranstr+imgSrc.name
            if len(res)==0:
                print("插入图片")
                print(imgSrc.name)

                with open("./media/"+msgpathname,'wb') as f:
                    for c in imgSrc.chunks():
                        f.write(c)
                # Message.objects.create(msgId=msgId, img=imgList[1:])
            else:
                pass
            Message.objects.create(msgId=msgId, linkman=user, contactWay=phone, price=float(price), detail=detail,img=msgpathname)
            result["message"] = 'Success'
            result["data"]="hello"
        except Exception as e:
            print(e)
            result["message"]=str(e)
        return JsonResponse(result,safe=False)



def returnList(request,category,sort="default"):#提交信息 二手信息、任务信息 排序方式
    result = {
        "status": 500,
        "message": None,
        "data": None
    }
    list_meg=[]
    #TODO: 返回二手信息列表
    list_meg=Message.objects.all()
    result['data']=list_meg
    return HttpResponse(str(result))
