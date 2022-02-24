 class AuthenticationManager {
    private static String savedPassword = "";
    private String fingerPrint = "";

    public void setSavedPassword(String s){
        this.savedPassword = s;
    }
    public void setFingerPrint(String s){
        this.fingerPrint = s;
    };

    public  boolean verifyPassword(String entered_password){
        System.out.println(savedPassword + " From authentication manager");
        return entered_password.equals(savedPassword);
    };
    //public  boolean verifyFingerPrint(String fingerPrint);
}
