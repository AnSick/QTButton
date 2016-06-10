package qt.qtbutton.model;

/**
 * Created by horo on 10.06.2016.
 */
public class Friend {
    String name;
    String telephone;
    public Friend(String telephone, String name) {
        this.telephone = telephone;
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
