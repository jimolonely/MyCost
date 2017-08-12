#-*-coding:utf-8-*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class Test(models.Model):
    name = models.CharField(max_length=20)

'''
花销收入条记录
'''
class CostRecord(models.Model):
    cid = models.IntegerField()
    c_date = models.CharField(max_length=100)
    in_out = models.IntegerField()
    money = models.FloatField()
    remark = models.CharField(max_length=255,null=True)
    sync_type = models.IntegerField()
    type_name = models.CharField(max_length=100)
    user_name = models.CharField(max_length=255)
    modify_time = models.IntegerField(help_text='数字时间戳')

'''
月记录
'''
class MonthCost(models.Model):
    mid = models.IntegerField()
    in_out = models.IntegerField()
    money = models.FloatField()
    month = models.IntegerField()
    year = models.IntegerField()
    sync_type = models.IntegerField()
    user_name = models.CharField(max_length=255)
    modify_time = models.IntegerField()

