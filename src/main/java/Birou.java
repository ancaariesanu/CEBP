import java.util.ArrayList;

public class Birou {
    private String Name;
    public ArrayList<Ghiseu> list;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public Birou(String Name)
    {
        this.Name=Name;
        //here is a method for creating new Birou's // office
    }
}
