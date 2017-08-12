from django.http import HttpResponse

def hello(request):
    return HttpResponse("hello mycost")

def logout(request):
    return HttpResponse('logout')