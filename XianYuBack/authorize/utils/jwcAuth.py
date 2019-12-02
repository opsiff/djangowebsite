import requests
import re
import re
headers={
    'Accept-Encoding': 'gzip, deflate',
    'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
    'Origin': 'http//jwch.fzu.edu.cn',
    'Proxy-Connection': 'keep-alive',
    'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36',
    'Referer': 'http://jwch.fzu.edu.cn/',
}
formData = {
    "muser": '041702324',
    "passwd": '10086KKDDaaggccd'
}
def parse(text):
    # print(text)
    name=re.findall(r'<td align="center"><span id="ContentPlaceHolder1_LB_xm">(.+)</span></td>',text)[0]
    number=re.findall(r'<td align="center"><span id="ContentPlaceHolder1_LB_xh">(.+)</span></td>',text)[0]
    sex=re.findall(r'<td align="center"><span id="ContentPlaceHolder1_LB_xb">(.+)</span></td>',text)[0]
    birth=re.findall(r'<td align="center"><span id="ContentPlaceHolder1_LB_csrq">(.+)</span></td>',text)[0]
    college=re.findall(r'<td colspan="3"><span id="ContentPlaceHolder1_LB_xymc">(.+)</span></td>',text)[0]
    major=re.findall(r'<td colspan="2"><span id="ContentPlaceHolder1_LB_zymc">(.+)</span></td>',text)[0]
    grade=re.findall(r'<td><span id="ContentPlaceHolder1_LB_nj">(.+)</span></td>',text)[0]
    dic={
        "name":name,
        "number":int(number),
        "sex":sex,
        "birth":birth,
        "college":college,
        "major":major,
        "grade":int(grade)
    }
    # print(dic)
    return dic

def request(formData):
    try:
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
        return parse(res.text)
    except:
        return -1


if __name__=="__main__":
    request(formData)