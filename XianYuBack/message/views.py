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
            msgId=str(int(round(time.time())*100000))
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
            if len(res)==0:
                print("插入图片")
                with open("./media/"+imgSrc.name,'wb') as f:
                    for c in imgSrc.chunks():
                        f.write(c)
                # Message.objects.create(msgId=msgId, img=imgList[1:])
            else:
                pass
            # Message.objects.create(msgId=msgId, linkman=user, contactWay=phone, price=float(price), detail=detail)
            result["message"] = 'Success'
            result["data"]="hello"
        except Exception as e:
            print(e)
            result["message"]=str(e)
        return JsonResponse(result,safe=False)



def returnList(request,category,sort):
    pass
