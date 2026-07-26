public class CustomerLinkedList {

    // ── Inner Node class ──────────────────────────────────────────────────────
    class Node {
        Customer data;
        Node next;
    }

    // ── Head pointer ──────────────────────────────────────────────────────────
    Node head;

    // ── Add a customer at the end of the list ─────────────────────────────────
    public void addCustomer(Customer c) {
        Node newNode = new Node();
        newNode.data = c;
        newNode.next = null;

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        System.out.println("Customer added: " + c.name);
    }

    // ── Display all customers ─────────────────────────────────────────────────
    public void displayAll() {
        if (head == null) {
            System.out.println("No customers found.");
            return;
        }
        System.out.println("\n===== Customer List =====");
        Node temp = head;
        while (temp != null) {
            System.out.println("ID    : " + temp.data.id);
            System.out.println("Name  : " + temp.data.name);
            System.out.println("Phone : " + temp.data.phone);
            System.out.println("-------------------------");
            temp = temp.next;
        }
    }

    // ── Find a customer by name — prints message if not found ────────────────
    public Customer findByName(String name) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.name.equalsIgnoreCase(name)) return temp.data;
            temp = temp.next;
        }
        System.out.println("Customer not found: " + name);
        return null;
    }

    // ── Silent version — returns null quietly, no console output ─────────────
    // Used internally by placeOrder to check existence before auto-registering,
    // without printing a confusing "not found" message to the customer.
    public Customer findByNameSilent(String name) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.name.equalsIgnoreCase(name)) return temp.data;
            temp = temp.next;
        }
        return null;
    }
}
