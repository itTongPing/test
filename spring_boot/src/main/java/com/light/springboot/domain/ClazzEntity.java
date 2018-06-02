package com.light.springboot.domain;

import java.util.List;

public class ClazzEntity {

	
	private int clazzID;

    private String clazzName;

    private List<StudentEntity> studentList;

    public int getClassID() {
        return clazzID;
    }

    public int getClazzID() {
        return clazzID;
    }

    public void setClazzID(int clazzID) {
        this.clazzID = clazzID;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<StudentEntity> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }
}
