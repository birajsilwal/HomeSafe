import java.util.Scanner;

public class InputController extends AuthenticationManager{
    String entered_password = "";
    String finger_print = "";

    public void readKey(String s){
        this.entered_password += s;
    }
    public void finger_print(String s){
        this.finger_print = s;
    }

    public boolean checkPassword() {
        return verifyPassword(entered_password);
    }
    public boolean checkResetPin(){
        return false;
    }
}
