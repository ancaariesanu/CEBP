import java.util.List;

public class BureaucracyManager {
    private final List<Office> offices;

    public BureaucracyManager(List<Office> offices) {
        this.offices = offices;
    }

    public Office getNextOffice(Customer customer) {
        for (Office office : offices) {
            for (Document doc : office.getDocuments()) {
                if (!customer.hasDocument(doc) && doc.canBeIssued(customer)) {
                    return office;
                }
            }
        }
        return null;
    }

    public boolean isDone(Customer customer) {
        for (Office office : offices) {
            for (Document doc : office.getDocuments()) {
                if (!customer.hasDocument(doc) && doc.canBeIssued(customer)) {
                    return false; // Customer still needs more documents
                }
            }
        }
        return true; // All required documents have been collected
    }
}
