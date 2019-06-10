package utils.model;

/**
 * Created by EalenXie on 2019/6/6 10:03.
 */
public class TargetModel {

    private String name;
    private String email;
    private String telephone;
    private String password;
    private Integer age;

    public TargetModel() {
    }

    public TargetModel(String name, String email, String telephone, String password, Integer age) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TargetModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
