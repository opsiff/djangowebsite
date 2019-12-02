from django.db import models

# Create your models here.
class Message(models.Model):
    msgId=models.CharField(max_length=30,primary_key=True) #信息ID,
    linkman=models.CharField(max_length=20) #
    contactWay=models.CharField(max_length=30)
    price=models.FloatField()
    # title=models.CharField(max_length=40) #标题不超过20字
    detail=models.CharField(max_length=240) #细节描述不超过120字
    img=models.CharField(max_length=200)
    post_date=models.DateTimeField(auto_now_add=True) #提交日期
    last_date=models.DateTimeField(auto_now=True) #最后修改日期
    def __str__(self):
        listmsg=[]
        listmsg['msgId'] = self.msgId
        listmsg['linkman'] = self.linkman
        listmsg['contactWay'] = self.contactWay
        listmsg['price'] = self.price
        listmsg['detail'] = self.detail
        listmsg['img'] = self.img
        listmsg['post_date'] = self.linkman
        listmsg['last_date'] = self.linkman
        return listmsg
