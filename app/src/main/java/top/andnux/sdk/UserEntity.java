package top.andnux.sdk;

import androidx.annotation.Keep;

import java.io.Serializable;

import top.andnux.utils.sqlite.annotation.Entity;
import top.andnux.utils.sqlite.annotation.Property;

@Entity("tb_user")
@Keep
public class UserEntity implements Serializable {
    @Property(value = "_id", autoincrement = true, primaryKey = true)
    private Integer id;
    @Property("name")
    private String name;
    @Property("age")
    private int age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
