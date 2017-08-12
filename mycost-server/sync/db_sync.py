#-*-coding:utf-8-*-
from django.http import HttpResponse
from sync.models import Test,CostRecord,MonthCost
from django.core import serializers
from django.views.decorators.csrf import csrf_exempt
import json

def testdb(request):
    test1 = Test(name='jimo')
    test1.save()
    return HttpResponse("save ok")

def getdb(reuqest):
    # 初始化
    response = ""
    response1 = ""
    
    
    # 通过objects这个模型管理器的all()获得所有数据行，相当于SQL中的SELECT * FROM
    list = Test.objects.all()
        
    # filter相当于SQL中的WHERE，可设置条件过滤结果
    response2 = Test.objects.filter(id=1) 
    
    # 获取单个对象
    response3 = Test.objects.get(id=1) 
    
    # 限制返回的数据 相当于 SQL 中的 OFFSET 0 LIMIT 2;
    Test.objects.order_by('name')[0:2]
    
    #数据排序
    Test.objects.order_by("id")
    
    # 上面的方法可以连锁使用
    Test.objects.filter(name="runoob").order_by("id")
    
    # 输出所有数据
    for var in list:
        response1 += var.name + " "
    response = response1
    return HttpResponse("<p>" + response + "</p>")

# 数据库操作
def updatedb(request):
    # 修改其中一个id=1的name字段，再save，相当于SQL中的UPDATE
    test1 = Test.objects.get(id=1)
    test1.name = 'Google'
    test1.save()
    
    # 另外一种方式
    #Test.objects.filter(id=1).update(name='Google')
    
    # 修改所有的列
    # Test.objects.all().update(name='Google')
    
    return HttpResponse("<p>修改成功</p>")

# 数据库操作
def deletedb(request):
    # 删除id=1的数据
    test1 = Test.objects.get(id=1)
    test1.delete()
    
    # 另外一种方式
    # Test.objects.filter(id=1).delete()
    
    # 删除所有数据
    # Test.objects.all().delete()
    
    return HttpResponse("<p>删除成功</p>")


def get_json(request):

    test = Test.objects.all()
    data = serializers.serialize('json',test)
    return HttpResponse(data)

#去掉CRSF验证
@csrf_exempt
def de_json(request):
    if request.method == 'POST':
        d = json.loads(request.body)
        re = ''
        for obj in d:
            re += obj['age']
        return HttpResponse(re)
    return HttpResponse('no')

@csrf_exempt
def upload(request):
    if request.method == 'POST':
        name = request.POST['username']
        data = request.POST['data']
        d = json.loads(data,encoding='utf-8')
        for i in d:
            c = getCostModel(i)
            c.save()
        return HttpResponse(d)
    return HttpResponse('no')

import time

'''
根据dict返回model
'''
def getCostModel(d):
    return CostRecord(cid=d['id'],c_date=d['date'],in_out=d['inOut'],money=d['money'],remark=d['remark'],sync_type=d['syncType'],type_name=d['typeName'],user_name=d['userName'],modify_time=time.time()) 