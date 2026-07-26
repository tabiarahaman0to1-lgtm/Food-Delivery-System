public class FoodBST {

    // ── Inner Node class ──────────────────────────────────────────────────────
    class Node {
        FoodItem data;
        Node left;
        Node right;
    }

    Node root;

    // ── Insert a FoodItem sorted by price ─────────────────────────────────────
    public void insert(FoodItem item) {
        root = insertRec(root, item);
        System.out.println("Food item added: " + item.name + " ($" + item.price + ")");
    }

    private Node insertRec(Node current, FoodItem item) {
        if (current == null) {
            Node n  = new Node();
            n.data  = item;
            n.left  = null;
            n.right = null;
            return n;
        }
        if (item.price < current.data.price)
            current.left  = insertRec(current.left,  item);
        else
            current.right = insertRec(current.right, item);
        return current;
    }

    // ── Show ALL items sorted by price (inorder) ──────────────────────────────
    public void showAllItems() {
        if (root == null) { System.out.println("Menu is empty."); return; }
        System.out.println("\n========== ALL FOOD ITEMS (cheapest first) ==========");
        showAllRec(root);
        System.out.println("=====================================================");
    }

    private void showAllRec(Node current) {
        if (current == null) return;
        showAllRec(current.left);
        System.out.println("  [" + current.data.id + "] " + current.data.name
            + " — $" + String.format("%.2f", current.data.price)
            + "  ★ " + current.data.rating
            + "  (Restaurant ID: " + current.data.restaurantId + ")");
        showAllRec(current.right);
    }

    // ── Show items belonging to ONE restaurant (filter during traversal) ───────
    public void showItemsByRestaurant(int restaurantId) {
        System.out.println("\n===== Menu for Restaurant ID: " + restaurantId + " =====");
        boolean[] found = { false };
        showByRestaurantRec(root, restaurantId, found);
        if (!found[0]) System.out.println("  No items found for this restaurant.");
        System.out.println("==========================================");
    }

    private void showByRestaurantRec(Node current, int restaurantId, boolean[] found) {
        if (current == null) return;
        showByRestaurantRec(current.left,  restaurantId, found);
        if (current.data.restaurantId == restaurantId) {
            System.out.println("  [" + current.data.id + "] " + current.data.name
                + " — $" + String.format("%.2f", current.data.price)
                + "  ★ " + current.data.rating);
            found[0] = true;
        }
        showByRestaurantRec(current.right, restaurantId, found);
    }

    // ── Search a single item by name (case-insensitive) ───────────────────────
    public FoodItem searchByName(String name) {
        return searchByNameRec(root, name.trim());
    }

    private FoodItem searchByNameRec(Node current, String name) {
        if (current == null) return null;
        if (current.data.name.equalsIgnoreCase(name)) return current.data;
        FoodItem left = searchByNameRec(current.left, name);
        if (left != null) return left;
        return searchByNameRec(current.right, name);
    }

    // ── Search item by name but only within a specific restaurant ─────────────
    public FoodItem searchByNameAndRestaurant(String name, int restaurantId) {
        return searchByNameRestaurantRec(root, name.trim(), restaurantId);
    }

    private FoodItem searchByNameRestaurantRec(Node current, String name, int restaurantId) {
        if (current == null) return null;
        if (current.data.name.equalsIgnoreCase(name)
                && current.data.restaurantId == restaurantId)
            return current.data;
        FoodItem left = searchByNameRestaurantRec(current.left, name, restaurantId);
        if (left != null) return left;
        return searchByNameRestaurantRec(current.right, name, restaurantId);
    }

    // ── Auto-calculate bill from item names for a given restaurant ────────────
    public double calculateBill(String[] itemNames, int restaurantId) {
        double total = 0;
        System.out.println("\n===== Bill Breakdown =====");
        for (String raw : itemNames) {
            String name = raw.trim();
            FoodItem item = searchByNameAndRestaurant(name, restaurantId);
            if (item != null) {
                System.out.println("  " + item.name + " ........... $" + String.format("%.2f", item.price));
                total += item.price;
            } else {
                System.out.println("  \"" + name + "\" — not found in this restaurant's menu (skipped)");
            }
        }
        System.out.println("--------------------------");
        System.out.println("  TOTAL : $" + String.format("%.2f", total));
        System.out.println("==========================");
        return total;
    }

    // ── Most popular item (highest rating) ────────────────────────────────────
    public FoodItem findPopular() {
        if (root == null) { System.out.println("Menu is empty."); return null; }
        FoodItem best = findPopularRec(root, root.data);
        System.out.println("\n===== Most Popular Item =====");
        System.out.println("  Name   : " + best.name);
        System.out.println("  Price  : $" + String.format("%.2f", best.price));
        System.out.println("  Rating : " + best.rating + " / 5.0");
        System.out.println("=============================");
        return best;
    }

    private FoodItem findPopularRec(Node current, FoodItem best) {
        if (current == null) return best;
        if (current.data.rating > best.rating) best = current.data;
        best = findPopularRec(current.left,  best);
        best = findPopularRec(current.right, best);
        return best;
    }
}
