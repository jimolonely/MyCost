from django.contrib import admin

# Register your models here.

from models import CostRecord,MonthCost

admin.site.register(CostRecord)

admin.site.register(MonthCost)

