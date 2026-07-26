public class OrderStack {

    // ── Inner Node class ──────────────────────────────────────────────────────
    class Node {
        Order data;
        Node next;
    }

    // ── Top pointer ───────────────────────────────────────────────────────────
    Node top;

    // ── Check if stack is empty ───────────────────────────────────────────────
    public boolean isEmpty() {
        return top == null;
    }

    // ── Push an order onto the top of the stack ───────────────────────────────
    public void push(Order o) {
        Node newNode = new Node();
        newNode.data = o;
        newNode.next = top;   // new node points to old top
        top          = newNode;
        System.out.println("Order pushed to history — Order ID: " + o.orderId);
    }

    // ── Pop (remove & return) the top order ───────────────────────────────────
    public Order pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty. No order to undo.");
            return null;
        }
        Order removed = top.data;
        top = top.next;
        System.out.println("Undo successful — Removed Order ID: " + removed.orderId);
        return removed;
    }

    // ── Peek at the top order without removing it ─────────────────────────────
    public Order peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return null;
        }
        return top.data;
    }

    // ── Display all orders in the stack (history) ─────────────────────────────
    public void displayStack() {
        if (isEmpty()) {
            System.out.println("Order history is empty.");
            return;
        }
        System.out.println("\n===== Order History (Stack) =====");
        Node temp = top;
        while (temp != null) {
            System.out.println("Order ID  : " + temp.data.orderId);
            System.out.println("Customer  : " + temp.data.customerName);
            System.out.println("Items     : " + temp.data.itemsList);
            System.out.println("Bill      : $" + temp.data.totalBill);
            System.out.println("Status    : " + temp.data.status);
            System.out.println("---------------------------------");
            temp = temp.next;
        }
    }

    // ── Find the most recent order for a specific customer ────────────────────
    // Stack is LIFO so top = most recent. First match = that customer's latest.
    // BUG 3 FIX: allows generateReceipt to look up any customer by name,
    // not just whoever placed the very last order overall.
    public Order findLatestByCustomer(String customerName) {
        Node temp = top;
        while (temp != null) {
            if (temp.data.customerName.equalsIgnoreCase(customerName)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }
}
