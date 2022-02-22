 class AuthenticationManager {
    private static String savedPassword = "123456";
    private String fingerPrint = "";

    public void setSavedPassword(String s){
        this.savedPassword = s;
    }
    public void setFingerPrint(String s){
        this.fingerPrint = s;
    };

    public  boolean verifyPassword(String entered_password){
        System.out.println(savedPassword);
        return entered_password.equals(savedPassword);
    };
    //public  boolean verifyFingerPrint(String fingerPrint);
}
