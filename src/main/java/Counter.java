import java.util.ArrayList;


public class Counter {
    private String Name;
    public ArrayList<Documents> documentList;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Counter(String name, ArrayList<Documents> documentList)
    {
        Name = name;
        this.documentList = documentList;
        // here we generate the new counter
    }

    public void issueDocument()
    {
        // here we call the document method to solve the issue
    }

    public void goOnBreak(int time)
    {
        // function to make the counter go on break for a set ammount of time
    }

    public void resumeWork()
    {
        // function to make the counter come back from the break
    }

    public void postBreakAnnouncement()
    {
        // function to announce the server that the counter is closed due to the break.
    }


}
