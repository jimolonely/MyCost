"""mycost URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from . import view
from sync import db_sync

urlpatterns = [
    url(r'admin/',admin.site.urls),
 #   url(r'admin/$logout/$/$',view.logout),
    url(r'^$',view.hello),
    url(r'^getdb$',db_sync.getdb),
    url(r'^updatedb$',db_sync.updatedb),
    url(r'^deletedb$',db_sync.deletedb),
    url(r'^get_json$',db_sync.get_json),
    url(r'^de_json$',db_sync.de_json),
    url(r'^upload$',db_sync.upload),
]
