package com.glen.common;


public class User {
    private String id;
    private String password;
    private String nickname;


    public User(String id, String nickname) {
        this.id = id;
        if (nickname == null||nickname.equals("")) {
            this.nickname = "未命名";
        } else {
            this.nickname = nickname;
        }
    }
    public User(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName()
                + "[id=" + this.id
                + ",pwd=" + this.password
                + ",nickname=" + this.nickname
                + "]";
    }
}
