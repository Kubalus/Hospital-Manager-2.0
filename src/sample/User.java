package sample;

public class User {
    private static String PESEL;

    public User(String PESEL)
    {
        this.PESEL = PESEL;
    }

    public static String getPESEL() {
        return PESEL;
    }
}
