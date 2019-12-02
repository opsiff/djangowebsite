from django.contrib import admin
from django.urls import path
from . import views
"""
路由到一下地址
/authorize/loginIn  登入
/authorize/loginOut 注销

"""
app_name='authorize'
urlpatterns = [
    path('loginIn',views.loginIn),
    path('loginOut',views.loginOut)
]
