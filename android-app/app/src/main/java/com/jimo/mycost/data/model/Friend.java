package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

@Table(name = "friend")
public class Friend {

    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "name")
    private String name;
    @Column(name = "id_num")
    private String idNum;//身份证号
    @Column(name = "sex")
    private String sex;
    @Column(name = "speciality")
    private String speciality;//专业
    @Column(name = "school")
    private String school;
    @Column(name = "address")
    private String address;//来自哪里
    @Column(name = "contact")
    private String contact;//联系方式,包括手机,邮箱等
    @Column(name = "height")
    private Float height;
    @Column(name = "weight")
    private Float weight;
    @Column(name = "hobby")
    private String hobby;
    @Column(name = "relation")
    private String relation;//和我的关系
    @Column(name = "score")
    private Float score;//我们关系评分
    @Column(name = "remark")
    private String remark; //备注
    @Column(name = "user_name")
    private String userName;
    @Column(name = "record_time")
    private Date recordTime;

    public Friend() {
    }

    public Friend(String birthday, String name, String idNum, String sex, String speciality,
                  String school, String address, String contact, Float height, Float weight,
                  String hobby, String relation, Float score, String remark) {
        this.birthday = birthday;
        this.name = name;
        this.idNum = idNum;
        this.sex = sex;
        this.speciality = speciality;
        this.school = school;
        this.address = address;
        this.contact = contact;
        this.height = height;
        this.weight = weight;
        this.hobby = hobby;
        this.relation = relation;
        this.score = score;
        this.remark = remark;
        this.userName = "jimo";
        this.recordTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}
