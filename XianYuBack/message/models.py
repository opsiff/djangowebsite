from django.db import models

# Create your models here.
class Message(models.Model):
    msgId=models.CharField(max_length=30,primary_key=True) #信息ID,
    linkman=models.CharField(max_length=20) #
    userID=models.CharField(max_length=20,default='123456789')
    contactWay=models.CharField(max_length=30)
    price=models.FloatField()
    # title=models.CharField(max_length=40) #标题不超过20字
    detail=models.CharField(max_length=240) #细节描述不超过120字
    img=models.CharField(max_length=200)
    post_date=models.DateTimeField(auto_now_add=True) #提交日期
    last_date=models.DateTimeField(auto_now=True) #最后修改日期
    isDel=models.BooleanField(default=False)
    def __str__(self):
        listmsg={}
        listmsg['msgId'] = self.msgId
        listmsg['linkman'] = self.linkman
        listmsg['contactWay'] = self.contactWay
        listmsg['price'] = self.price
        listmsg['detail'] = self.detail
        listmsg['img'] = self.img
        listmsg['post_date'] = self.post_date
        listmsg['last_date'] = self.last_date
        return str(listmsg)
