package kuan.com.timetable.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-34 on 2016/12/2 0002.
 */

public class PersonBean extends BmobObject {
    private String name;
    private String passwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
