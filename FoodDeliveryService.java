public class FoodDeliveryService {

    CustomerLinkedList   customers   = new CustomerLinkedList();
    RestaurantLinkedList restaurants = new RestaurantLinkedList();
    OrderQueue           orderQueue  = new OrderQueue();
    OrderStack           orderStack  = new OrderStack();
    FoodBST              foodMenu    = new FoodBST();

    int orderCounter    = 1;
    int customerIdCounter = 101;  // IDs for auto-registered customers

    // =========================================================================
    // STEP 1 — Browse ALL food items (BST inorder: cheap to expensive)
    // =========================================================================
    public void browseAllItems() {
        foodMenu.showAllItems();
    }

    // =========================================================================
    // STEP 2 — Show all restaurants
    // =========================================================================
    public void showRestaurants() {
        restaurants.displayAll();
    }

    // =========================================================================
    // STEP 3 — View one restaurant's menu
    // =========================================================================
    public void showMenuByRestaurant(int restaurantId) {
        // BUG 2 FIX: show the restaurant name/info alongside the items
        Restaurant r = restaurants.findById(restaurantId);
        if (r == null) {
            System.out.println("No restaurant found with ID: " + restaurantId);
            System.out.println("Use option 2 to see all restaurant IDs.");
            return;
        }
        System.out.println("\n  Restaurant : " + r.name + " (" + r.location + ")");
        foodMenu.showItemsByRestaurant(restaurantId);
    }

    // =========================================================================
    // STEP 4 — Place order: auto-bill from BST, auto-register customer
    // =========================================================================
    public void placeOrder(String customerName, String itemsCSV, int restaurantId) {

        // BUG 2 FIX: validate restaurant exists before accepting order
        Restaurant r = restaurants.findById(restaurantId);
        if (r == null) {
            System.out.println("Restaurant ID " + restaurantId + " does not exist.");
            System.out.println("Use option 2 to see valid restaurant IDs.");
            return;
        }

        String[] itemNames = itemsCSV.split(",");
        double bill = foodMenu.calculateBill(itemNames, restaurantId);

        if (bill == 0) {
            System.out.println("None of the items were found in " + r.name + "'s menu. Order cancelled.");
            System.out.println("Use option 3 to view that restaurant's menu first.");
            return;
        }

        // BUG 4 FIX: auto-register customer with phone collected at order time
        // (phone is passed in from Main — see handlePlaceOrder)
        // We call the overloaded version below
        Customer existing = customers.findByNameSilent(customerName);
        if (existing == null) {
            System.out.print("Enter your phone number: ");
            // Phone is collected in Main and passed here — see placeOrder(name,phone,items,rid)
        }

        Order o         = new Order();
        o.orderId       = orderCounter++;
        o.customerName  = customerName;
        o.itemsList     = itemsCSV.trim();
        o.totalBill     = bill;
        o.status        = "Order Placed";
        o.estimatedTime = 20 + (o.orderId * 7) % 26;

        orderQueue.enqueue(o);
        orderStack.push(o);

        System.out.println("\nOrder #" + o.orderId + " confirmed for " + customerName
            + " at " + r.name);
        System.out.println("   Total        : $" + String.format("%.2f", bill));
        System.out.println("   Est. Delivery: " + o.estimatedTime + " minutes");
    }

    // Overloaded placeOrder — takes phone number for new customer registration (BUG 4 FIX)
    public void placeOrder(String customerName, String phone, String itemsCSV, int restaurantId) {

        Restaurant r = restaurants.findById(restaurantId);
        if (r == null) {
            System.out.println("Restaurant ID " + restaurantId + " does not exist.");
            return;
        }

        String[] itemNames = itemsCSV.split(",");
        double bill = foodMenu.calculateBill(itemNames, restaurantId);

        if (bill == 0) {
            System.out.println("None of the items were found in " + r.name + "'s menu. Order cancelled.");
            return;
        }

        // Auto-register customer with their real phone number
        Customer existing = customers.findByNameSilent(customerName);
        if (existing == null) {
            Customer nc  = new Customer();
            nc.id        = customerIdCounter++;
            nc.name      = customerName;
            nc.phone     = phone;          // BUG 4 FIX: real phone stored
            nc.feedback  = null;
            nc.lastBill  = bill;
            nc.lastOriginalBill = bill;
            customers.addCustomer(nc);
            System.out.println("New customer registered: " + customerName);
        } else {
            // Update their bill record even if they already exist
            existing.lastBill        = bill;
            existing.lastOriginalBill = bill;
            existing.lastCoupon      = null;
        }

        Order o         = new Order();
        o.orderId       = orderCounter++;
        o.customerName  = customerName;
        o.itemsList     = itemsCSV.trim();
        o.totalBill     = bill;
        o.status        = "Order Placed";
        o.estimatedTime = 20 + (o.orderId * 7) % 26;

        orderQueue.enqueue(o);
        orderStack.push(o);

        System.out.println("\nOrder #" + o.orderId + " confirmed for " + customerName
            + " at " + r.name);
        System.out.println("   Total        : $" + String.format("%.2f", bill));
        System.out.println("   Est. Delivery: " + o.estimatedTime + " minutes");
    }

    // =========================================================================
    // TRACK DELIVERY
    // =========================================================================
    public void trackDelivery() {
        Order o = orderQueue.dequeue();
        if (o == null) return;
        o.status = "Out for Delivery";
        System.out.println("Order #" + o.orderId + " for " + o.customerName + " is now: " + o.status);
        System.out.println("   Estimated arrival: " + o.estimatedTime + " minutes");
    }

    // =========================================================================
    // UNDO LAST ORDER
    // =========================================================================
    public void undoLastOrder() {
        Order o = orderStack.pop();
        if (o != null)
            System.out.println("Undone — Order #" + o.orderId + " (" + o.customerName + ") removed from history.");
    }

    // =========================================================================
    // SEARCH MENU ITEM BY NAME
    // =========================================================================
    public void searchMenuItem(String name) {
        System.out.println("Searching for: \"" + name + "\"");
        FoodItem item = foodMenu.searchByName(name);
        if (item == null) {
            System.out.println("Not found. Use option 1 to browse all items.");
        } else {
            Restaurant r = restaurants.findById(item.restaurantId);
            String rName = (r != null) ? r.name : "ID " + item.restaurantId;
            System.out.println("\n  " + item.name
                + " | $" + String.format("%.2f", item.price)
                + " | Rating: " + item.rating
                + " | Restaurant: " + rName);
        }
    }

    // =========================================================================
    // ORDER HISTORY
    // =========================================================================
    public void displayOrderHistory() {
        orderStack.displayStack();
    }

    // =========================================================================
    // ADD FOOD ITEM
    // BUG 2 FIX: validate restaurant ID exists; warn clearly if not
    // =========================================================================
    public void addFoodItem(FoodItem f) {
        Restaurant r = restaurants.findById(f.restaurantId);
        if (r == null) {
            System.out.println("Restaurant ID " + f.restaurantId + " does not exist.");
            System.out.println("Add the restaurant first using option 2, then add the food item.");
            System.out.println("Or use an existing restaurant ID (see option 2).");
            return;
        }
        foodMenu.insert(f);
        System.out.println("Item \"" + f.name + "\" added to " + r.name + "'s menu.");
    }

    // =========================================================================
    // ADD RESTAURANT (new — needed for bug 2 so new restaurants are registered)
    // =========================================================================
    public void addRestaurant(Restaurant r) {
        restaurants.addRestaurant(r);
    }

    // =========================================================================
    // ADD CUSTOMER (manual, e.g. from sample data)
    // =========================================================================
    public void addCustomer(Customer c) {
        customers.addCustomer(c);
    }

    // =========================================================================
    // SHOW CUSTOMER DETAILS
    // BUG 5 FIX: shows original bill, discounted bill, coupon used
    // =========================================================================
    public void showCustomerDetails(String name) {
        Customer c = customers.findByName(name);
        if (c == null) return;

        System.out.println("\n===== Customer Details =====");
        System.out.println("  ID            : " + c.id);
        System.out.println("  Name          : " + c.name);
        System.out.println("  Phone         : " + c.phone);
        System.out.println("  Feedback      : " + (c.feedback == null ? "None yet" : c.feedback));

        // BUG 1 + 5 FIX: show bill and discount info if available
        if (c.lastOriginalBill > 0) {
            System.out.println("  Last Bill     : $" + String.format("%.2f", c.lastOriginalBill));
            if (c.lastCoupon != null) {
                System.out.println("  Coupon Used   : " + c.lastCoupon);
                System.out.println("  After Discount: $" + String.format("%.2f", c.lastBill));
            }
        }
        System.out.println("============================");
    }

    // =========================================================================
    // ASSIGN DELIVERY PERSON
    // =========================================================================
    public void assignDeliveryPerson(int orderId, String personName) {
        System.out.println("\"" + personName + "\" assigned to Order #" + orderId);
    }

    // =========================================================================
    // ESTIMATED DELIVERY TIME
    // =========================================================================
    public int estimatedDeliveryTime(int orderId) {
        return 20 + (orderId * 7) % 26;
    }

    // =========================================================================
    // SAVE FEEDBACK
    // =========================================================================
    public void saveFeedback(String customerName, String feedback) {
        Customer c = customers.findByName(customerName);
        if (c == null) return;
        c.feedback = feedback;
        System.out.println("Feedback saved for " + customerName + ": \"" + feedback + "\"");
    }

    // =========================================================================
    // APPLY DISCOUNT
    // BUG 1 FIX: saves discounted price onto the Customer object so
    // showCustomerDetails can display it
    // =========================================================================
    public double applyDiscount(String customerName, String coupon) {
        Customer c = customers.findByName(customerName);
        if (c == null) return 0;

        if (c.lastOriginalBill == 0) {
            System.out.println("No order found for " + customerName + ". Place an order first.");
            return 0;
        }

        double original   = c.lastOriginalBill;
        double discounted = original;

        if (coupon.equalsIgnoreCase("SAVE10")) {
            discounted = original * 0.90;
            System.out.println("Coupon SAVE10 applied — 10% off!");
        } else if (coupon.equalsIgnoreCase("SAVE20")) {
            discounted = original * 0.80;
            System.out.println("Coupon SAVE20 applied — 20% off!");
        } else {
            System.out.println("Invalid coupon: \"" + coupon + "\". Valid codes: SAVE10, SAVE20");
            return original;
        }

        // BUG 1 FIX: write discounted bill back to customer record
        c.lastBill   = discounted;
        c.lastCoupon = coupon.toUpperCase();

        System.out.println("  Customer      : " + customerName);
        System.out.println("  Original Bill : $" + String.format("%.2f", original));
        System.out.println("  Discount      : -$" + String.format("%.2f", original - discounted));
        System.out.println("  Final Bill    : $" + String.format("%.2f", discounted));

        return discounted;
    }

    // =========================================================================
    // SHOW POPULAR ITEMS
    // =========================================================================
    public void showPopularItems() {
        foodMenu.findPopular();
    }

    // =========================================================================
    // GENERATE RECEIPT
    // BUG 3 FIX: takes customer name instead of using a hardcoded lastOrder,
    // so receipt can be generated for any named customer's most recent order
    // =========================================================================
    public void generateReceipt(String customerName) {
        // Walk the stack to find the most recent order for this customer
        // (Stack stores newest on top, so first match = most recent)
        Order found = orderStack.findLatestByCustomer(customerName);

        if (found == null) {
            System.out.println("No order found for customer: " + customerName);
            System.out.println("Note: Undone orders are removed from history.");
            return;
        }

        Customer c = customers.findByNameSilent(customerName);
        double finalBill = (c != null && c.lastCoupon != null) ? c.lastBill : found.totalBill;
        String couponLine = (c != null && c.lastCoupon != null)
            ? "  Coupon        : " + c.lastCoupon + " applied" : "";

        System.out.println();
        System.out.println("========================================");
        System.out.println("       FOOD DELIVERY RECEIPT");
        System.out.println("========================================");
        System.out.println("  Order ID      : #" + found.orderId);
        System.out.println("  Customer      : " + found.customerName);
        System.out.println("  Items         : " + found.itemsList);
        System.out.println("  Status        : " + found.status);
        System.out.println("  Est. Delivery : " + found.estimatedTime + " minutes");
        System.out.println("----------------------------------------");
        System.out.println("  Original Bill : $" + String.format("%.2f", found.totalBill));
        if (!couponLine.isEmpty()) {
            System.out.println(couponLine);
            System.out.println("  FINAL BILL    : $" + String.format("%.2f", finalBill));
        } else {
            System.out.println("  TOTAL BILL    : $" + String.format("%.2f", found.totalBill));
        }
        System.out.println("========================================");
        System.out.println("      Thank you for your order!");
        System.out.println("========================================\n");
    }
}
