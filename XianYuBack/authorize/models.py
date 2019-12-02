from django.db import models

# Create your models here.


class OnlineUser(models.Model):#维护一个token表
    account = models.CharField(max_length=9, primary_key=True)
    token = models.CharField(max_length=27)



class User(models.Model): #已经注册的用户

    account=models.CharField(max_length=9,primary_key=True)
    passwd=models.CharField(max_length=20)

    def __str__(self):
        return  self.account

class PersonalInformatin(models.Model): #用户个人信息存储
    name=models.CharField(max_length=20,default="赵四")
    number=models.CharField(max_length=9,primary_key=True,default=123456789)
    sex=models.CharField(max_length=4,default="男")
    birth=models.CharField(max_length=10,default='string')
    college=models.CharField(max_length=30 ,default='SOME STRING')
    major=models.CharField(max_length=30,default='SOME STRING')
    grade=models.IntegerField(default=0)



