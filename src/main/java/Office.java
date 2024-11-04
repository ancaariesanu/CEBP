import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Office {
    private final int officeId;
    private final List<Document> documents;
    private final BlockingQueue<Customer> queue;
    private final ReentrantLock countersLock = new ReentrantLock();
    private final ExecutorService counterExecutor;
    private volatile boolean isCoffeeBreak;

    public Office(int officeId, List<Document> documents, int numberOfCounters) {
        this.officeId = officeId;
        this.documents = documents;
        this.queue = new LinkedBlockingQueue<>();
        this.counterExecutor = Executors.newFixedThreadPool(numberOfCounters);
        this.isCoffeeBreak = false;
    }

    public void joinQueue(Customer customer) throws InterruptedException {
        queue.put(customer); // Adds customer to the BlockingQueue, blocks if full
        System.out.println("Customer " + customer.getCustomerId() + " joined the queue at Office " + officeId);
    }

    public void startServing() {
        for (int i = 0; i < ((ThreadPoolExecutor) counterExecutor).getCorePoolSize(); i++) {
            counterExecutor.submit(() -> {
                while (!isCoffeeBreak) {
                    try {
                        countersLock.lock();
                        Customer customer = queue.take(); // Take a customer from the queue
                        serveCustomer(customer);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        countersLock.unlock();
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
