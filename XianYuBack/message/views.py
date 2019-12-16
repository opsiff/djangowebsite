from django.shortcuts import render
import json,time
import  os
from PIL import Image
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
            linkman=request.POST["user"]
            userID=request.POST["userID"]
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
                filename="./media/"+msgpathname
                with open(filename,'wb') as f:
                    for c in imgSrc.chunks():
                        f.write(c)
                # Message.objects.create(msgId=msgId, img=imgList[1:])
#                img=Image.open(filename)
#                w,h=img.size
#                img.resize((w/2,h/2),Image.ANTIALIAS)
#                img.save(filename,'jpeg',quality=95)
            else:
                pass
            Message.objects.create(msgId=msgId, linkman=linkman,userID=userID, contactWay=phone, price=float(price), detail=detail,img=msgpathname)
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
    qs = Message.objects.filter(isDel=False)
#    list_meg=Message.objects.filter(price = 1000000)
    count=0
    for i in qs:
        dict = {'msgId': i.msgId, 'linkman': i.linkman, 'contactWay': i.contactWay,'price': i.price, 'detail': i.detail,'img': i.img, 'post_date': str(i.post_date), 'last_date': str(i.last_date)}
        list_meg.append(dict)
        count+=1
        if count>=20:
        	break;
        

    result['data']=list_meg
    print(json.dumps(result, ensure_ascii=False))
    
    return HttpResponse(json.dumps(result, ensure_ascii=False))
    
def returnimg(request,category):
    if request.method=="GET":
        try:
#            print (os.getcwd())
            filename=request.GET.get('img')
            f=open( os.getcwd()+'/media/'+filename, 'rb+' )
            imgSrc=f.read()
#            print(imgSrc)
            f.close()
            result=imgSrc
            return HttpResponse(result)
        except Exception as e:
            print(e)

def search(request,category,sort="default"):  # 提交信息 二手信息、任务信息 排序方式
    result = {
        "status": 500,
        "message": None,
        "data": None
    }
    list_meg = []
    if request.method == "POST":
        linkman = request.POST["Query"]
        qs = Message.objects.filter(linkman=linkman,isDel=False)
        count = 0
        for i in qs:
            dict = {'msgId': i.msgId, 'linkman': i.linkman, 'contactWay': i.contactWay, 'price': i.price,'detail': i.detail, 'img': i.img, 'post_date': str(i.post_date), 'last_date': str(i.last_date)}
            list_meg.append(dict)
            count += 1
            if count >= 20:
                break
        result['data'] = list_meg
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))
    else:
        result['data'] = list_meg
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))

def returnMyList(request,category,sort="default"):#提交信息 二手信息、任务信息 排序方式
    result = {
        "status": 500,
        "message": None,
        "data": None
    }
    list_meg=[]
    if request.method == "POST":
        userID = request.POST["userID"]
        qs = Message.objects.filter(userID=userID,isDel=False)
        count = 0
        for i in qs:
            dict = {'msgId': i.msgId, 'linkman': i.linkman, 'contactWay': i.contactWay, 'price': i.price,'detail': i.detail, 'img': i.img, 'post_date': str(i.post_date), 'last_date': str(i.last_date)}
            list_meg.append(dict)
            count += 1
            if count >= 20:
                break
        result['data'] = list_meg
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))
    else:
        result['data'] = list_meg
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))

def dele(request,category,sort="default"):#提交信息 二手信息、任务信息 排序方式
    result = {
        "status": 500,
        "message": None,
        "data": None
    }
    list_meg=[]
    if request.method == "POST":
        msgId = request.POST["msgId"]
        qs = Message.objects.get(msgId=msgId)
        qs.isDel=True
        qs.save()
        result['message'] = 'success'
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))
    else:
        result['message'] = 'fail'
        print(json.dumps(result, ensure_ascii=False))
        return HttpResponse(json.dumps(result, ensure_ascii=False))


            




