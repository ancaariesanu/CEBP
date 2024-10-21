import java.util.ArrayList;

public class User {
    private String Name;
    private String Surname;
    public ArrayList<Birou> officeList;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
