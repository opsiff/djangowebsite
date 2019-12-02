from django.contrib import admin
from django.urls import path
from . import views
"""
路由到一下地址

/message/<category>/<id>/detail GET方式 拿到某一类的某个具体信息
/message/<categoyy>/submmit     POST方式
/message/<category>/list/<sort>  某一类按照某种方式排序的前30列表

"""
app_name='message'
urlpatterns = [
    path('<str:category>/<int:id>/detail',views.returnDetail,name='message'),
    path('<str:category>/submmit',views.submmit,name='message'),
    path('<str:category>/list/<int:sort>',views.returnList,name='message'),
]
