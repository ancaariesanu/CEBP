import java.util.ArrayList;

public class Office {
    private String Name;
    public ArrayList<Counter> list;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public Office(String Name)
    {
        this.Name=Name;
        //here is a method for creating new offices
    }
}
