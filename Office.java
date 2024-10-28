import java.util.List;
import java.util.concurrent.*;

public class Office {
    private final int officeId;
    private final List<Document> documents;
    private final BlockingQueue<Customer> queue;
    private final Semaphore countersSemaphore;
    private final ExecutorService counterExecutor;
    private volatile boolean isCoffeeBreak;

    public Office(int officeId, List<Document> documents, int numberOfCounters) {
        this.officeId = officeId;
        this.documents = documents;
        this.queue = new LinkedBlockingQueue<>();
        this.countersSemaphore = new Semaphore(numberOfCounters);
        this.counterExecutor = Executors.newFixedThreadPool(numberOfCounters);
        this.isCoffeeBreak = false;
    }

    public void joinQueue(Customer customer) throws InterruptedException {
        queue.put(customer); // Adds customer to the BlockingQueue, blocks if full
        System.out.println("Customer " + customer.getCustomerId() + " joined the queue at Office " + officeId);
    }

    public void startServing() {
        for (int i = 0; i < countersSemaphore.availablePermits(); i++) {
            counterExecutor.submit(() -> {
                while (!isCoffeeBreak) {
                    try {
                        countersSemaphore.acquire(); // Acquire a counter
                        Customer customer = queue.take(); // Take a customer from the queue
                        serveCustomer(customer);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        countersSemaphore.release(); // Release the counter after serving
                    }
                }
            });
        }
    }

    private void serveCustomer(Customer customer) {
        System.out.println("Serving customer " + customer.getCustomerId() + " at Office " + officeId);
        customer.receiveDocument(this);
    }

    public void closeCountersForCoffeeBreak() {
        isCoffeeBreak = true;
        System.out.println("Office " + officeId + " is closed for a coffee break.");
    }

    public void reopenAfterCoffeeBreak() {
        isCoffeeBreak = false;
        System.out.println("Office " + officeId + " has reopened after the coffee break.");
        startServing(); // Resume serving
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
