package com.eustress.moong;

public class Users {
    //변수 선언
    private String m_birth;
    private String m_name = "이름 생성 안됨";
    private String m_email;

    public Users(){}
    //여기서부터 get,set 함수를 사용하는데 이부분을 통해 값을 가져옴
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public String getBirth() {
        return m_birth;
    }

    public void setBirth(String birth) {
        this.m_birth = birth;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        this.m_email = email;
    }

    //이거는 그룹을 생성할때 사용하는 부분
    public Users(String name, String birth) {
        this.m_birth = birth;
        this.m_name = name;
    }
}
