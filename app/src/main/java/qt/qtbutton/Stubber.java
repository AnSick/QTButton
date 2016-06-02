package qt.qtbutton;

/**
 * Created by Надюша on 02.06.2016.
 */
public class Stubber {
    public boolean loginStub(String tel, String pass) {
        Boolean result;
        if ((tel.equals("11111")) && (pass.equals("1111"))) {
            return true;
        } else {
            return false;
        }
    }
}
