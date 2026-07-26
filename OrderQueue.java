public class OrderQueue {

    // ── Inner Node class ──────────────────────────────────────────────────────
    class Node {
        Order data;
        Node next;
    }

    // ── Front and rear pointers ───────────────────────────────────────────────
    Node front;
    Node rear;

    // ── Check if queue is empty ───────────────────────────────────────────────
    public boolean isEmpty() {
        return front == null;
    }

    // ── Add an order at the rear of the queue ─────────────────────────────────
    public void enqueue(Order o) {
        Node newNode = new Node();
        newNode.data = o;
        newNode.next = null;

        if (rear == null) {
            front = newNode;
            rear  = newNode;
        } else {
            rear.next = newNode;
            rear      = newNode;
        }
        System.out.println("Order placed in queue — Order ID: " + o.orderId);
    }

    // ── Remove and return the order at the front of the queue ─────────────────
    public Order dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. No orders to process.");
            return null;
        }
        Order removed = front.data;
        front = front.next;
        if (front == null) {
            rear = null;          // queue became empty
        }
        System.out.println("Processing order — Order ID: " + removed.orderId);
        return removed;
    }

    // ── Display all pending orders ────────────────────────────────────────────
    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("No pending orders in queue.");
            return;
        }
        System.out.println("\n===== Pending Orders (Queue) =====");
        Node temp = front;
        while (temp != null) {
            System.out.println("Order ID  : " + temp.data.orderId);
            System.out.println("Customer  : " + temp.data.customerName);
            System.out.println("Items     : " + temp.data.itemsList);
            System.out.println("Bill      : $" + temp.data.totalBill);
            System.out.println("Status    : " + temp.data.status);
            System.out.println("----------------------------------");
            temp = temp.next;
        }
    }
}
