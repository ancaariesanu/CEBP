import java.util.ArrayList;


public class Ghiseu {
    private String Name;
    public ArrayList<Documents> documentList;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Ghiseu(String name, ArrayList<Documents> documentList)
    {
        Name = name;
        this.documentList = documentList;
        //here we generate the new counter
    }

    public void issueDocument()
    {
        //here we call the document method to solve the issue
    }
}
