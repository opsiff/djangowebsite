import requests
import re


def request(username,password):
    headers={
        'Accept-Encoding': 'gzip, deflate',
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
        'Origin': 'http/ch.fzu.edu.cn',
        'Proxy-Connection': 'keep-alive',
        'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36',
        'Referer': 'http://jwch.fzu.edu.cn/',
    }
    formData = {
        "muser": username,
        "passwd": password
    }
    url = 'http://59.77.226.32/logincheck.asp'
    session=requests.session()  #创建一个会话
    response=session.post(url,headers=headers,data=formData) #post请求提交表单
    html=response.text
    #正则提取时间
    top=re.search(r'top\.aspx\?id=\d+',html).group()
    num = re.search(r'=\d+',top).group()[1:]
    headers_clone = headers #重新搞一个请求头
    # headers_clone['Referer']=left
    #发送get请求
    informatin="http://59.77.226.35/jcxx/xsxx/StudentInformation.aspx?id="+num
    res = session.get(informatin, headers=headers_clone)
    print(res.text)
    # print(type(res.text))

    # 学号
    pattern = re.search(r'<span id="ContentPlaceHolder1_LB_xh">([0-9]+)</span>',res.text)
    print(pattern.group(1))
    id=pattern.group(1)

    # 姓名
    pattern = re.search(r'<span id="ContentPlaceHolder1_LB_xm">(.+)</span>', res.text)
    print(pattern.group(1))
    name=pattern.group(1)

    # 性别
    pattern = re.search(r'<span id="ContentPlaceHolder1_LB_xb">(.+)</span>', res.text)
    print(pattern.group(1))
    sex=pattern.group(1)

    # 院系
    pattern = re.search(r'<span id="ContentPlaceHolder1_LB_xymc">(.+)</span>', res.text)
    print(pattern.group(1))
    dep=pattern.group(1)

    # 专业
    pattern = re.search(r'<span id="ContentPlaceHolder1_LB_zymc">(.+)</span>', res.text)
    print(pattern.group(1))
    major=pattern.group(1)

    d={}
    d['学号']=id
    d['姓名'] = name
    d['性别'] = sex
    d['院系'] = dep
    d['专业'] = major

    print(d)
    return d


if __name__=="__main__":
    request()
