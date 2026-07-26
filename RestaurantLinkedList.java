public class RestaurantLinkedList {

    // ── Inner Node class ──────────────────────────────────────────────────────
    class Node {
        Restaurant data;
        Node next;
    }

    // ── Head pointer ──────────────────────────────────────────────────────────
    Node head;

    // ── Add a restaurant at the end of the list ───────────────────────────────
    public void addRestaurant(Restaurant r) {
        Node newNode = new Node();
        newNode.data = r;
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
        System.out.println("Restaurant added: " + r.name);
    }

    // ── Display all restaurants ───────────────────────────────────────────────
    public void displayAll() {
        if (head == null) {
            System.out.println("No restaurants found.");
            return;
        }
        System.out.println("\n===== Available Restaurants =====");
        Node temp = head;
        while (temp != null) {
            System.out.println("ID       : " + temp.data.id);
            System.out.println("Name     : " + temp.data.name);
            System.out.println("Location : " + temp.data.location);
            System.out.println("---------------------------------");
            temp = temp.next;
        }
    }

    // ── Find restaurant by ID ─────────────────────────────────────────────────
    // Used when adding a new food item to verify the restaurant ID is valid,
    // and to show the restaurant name in the confirmation message.
    public Restaurant findById(int id) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.id == id) return temp.data;
            temp = temp.next;
        }
        return null;
    }
}
