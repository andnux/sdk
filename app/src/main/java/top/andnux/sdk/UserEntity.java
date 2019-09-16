package top.andnux.sdk;

import androidx.annotation.Keep;

import java.io.Serializable;

import top.andnux.sqlite.annotation.Entity;
import top.andnux.sqlite.annotation.Property;

@Keep
@Entity("tb_user")
public class UserEntity implements Serializable {

    @Property(value = "id", optional = "_id",
            autoincrement = true, primaryKey = true)
    private Integer id;
    @Property("name")
    private String name;
    @Property(value = "age", check = "age > 0")
    private Integer age;
    @Property(value = "id_card", unique = true, notNull = true)
    private String idCard;
    @Property(value = "sex", defaultValue = "0")
    private Integer sex;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", idCard='" + idCard + '\'' +
                ", sex=" + sex +
                '}';
    }
}
