#!/bin/bash
#--------------------------------------------
# author：jimolonely
# site：jimolonely.github.io
# slogan：shell is a way!
#--------------------------------------------

# 接受git commit 提交参数
msg="$*"

if [ -z "$msg" ];then
    msg=$(git status --short)
fi

git status
git add .
git commit -m "$msg"
git push
