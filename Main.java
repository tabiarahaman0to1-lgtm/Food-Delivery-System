import java.util.Scanner;

public class Main {

    static FoodDeliveryService service = new FoodDeliveryService();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadSampleData();

        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:  service.browseAllItems();      break;
                case 2:  service.showRestaurants();     break;
                case 3:  handleShowRestaurantMenu();    break;
                case 4:  handlePlaceOrder();            break;
                case 5:  service.trackDelivery();       break;
                case 6:  service.displayOrderHistory(); break;
                case 7:  service.undoLastOrder();       break;
                case 8:  handleGenerateReceipt();       break;  // BUG 3 FIX: ask customer name
                case 9:  handleSearchMenuItem();        break;
                case 10: service.showPopularItems();    break;
                case 11: handleAddRestaurant();         break;  // BUG 2 FIX: add restaurant first
                case 12: handleAddFoodItem();           break;
                case 13: handleShowCustomerDetails();   break;
                case 14: handleAssignDeliveryPerson();  break;
                case 15: handleEstimatedTime();         break;
                case 16: handleSaveFeedback();          break;
                case 17: handleApplyDiscount();         break;  // BUG 1 FIX: asks customer name + coupon
                case 18: System.out.println("Thank you! Goodbye."); break;
                default: System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        } while (choice != 18);

        sc.close();
    }

    // =========================================================================
    // MENU
    // =========================================================================
    static void printMenu() {
        System.out.println("==================================================");
        System.out.println("           FOOD DELIVERY SYSTEM");
        System.out.println("==================================================");
        System.out.println("  -- ORDER FLOW --");
        System.out.println("   1. Browse All Food Items");
        System.out.println("   2. Show All Restaurants");
        System.out.println("   3. View Menu of a Restaurant");
        System.out.println("   4. Place Order");
        System.out.println("  -- ORDER MANAGEMENT --");
        System.out.println("   5. Track Delivery");
        System.out.println("   6. Display Order History");
        System.out.println("   7. Undo Last Order");
        System.out.println("   8. Generate Order Receipt");
        System.out.println("  -- MENU & SEARCH --");
        System.out.println("   9. Search Food Item (by name)");
        System.out.println("  10. Show Popular Food Items");
        System.out.println("  11. Add New Restaurant");
        System.out.println("  12. Add New Food Item");
        System.out.println("  -- CUSTOMER & DELIVERY --");
        System.out.println("  13. Show Customer Details");
        System.out.println("  14. Assign Delivery Person");
        System.out.println("  15. Estimated Delivery Time");
        System.out.println("  16. Save Customer Feedback");
        System.out.println("  17. Apply Discount Coupon");
        System.out.println("  18. Exit");
        System.out.println("==================================================");
    }

    // =========================================================================
    // SAMPLE DATA
    // =========================================================================
    static void loadSampleData() {
        Restaurant r1 = new Restaurant();
        r1.id = 1; r1.name = "Burger Hub"; r1.location = "Dhanmondi";
        service.restaurants.addRestaurant(r1);

        Restaurant r2 = new Restaurant();
        r2.id = 2; r2.name = "Pizza Palace"; r2.location = "Gulshan";
        service.restaurants.addRestaurant(r2);

        FoodItem f1 = new FoodItem();
        f1.id = 1; f1.name = "Cheeseburger";   f1.price = 5.99; f1.rating = 4.5; f1.restaurantId = 1;
        service.foodMenu.insert(f1);

        FoodItem f2 = new FoodItem();
        f2.id = 2; f2.name = "French Fries";   f2.price = 2.49; f2.rating = 4.2; f2.restaurantId = 1;
        service.foodMenu.insert(f2);

        FoodItem f3 = new FoodItem();
        f3.id = 3; f3.name = "Chicken Wings";  f3.price = 7.49; f3.rating = 4.6; f3.restaurantId = 1;
        service.foodMenu.insert(f3);

        FoodItem f4 = new FoodItem();
        f4.id = 4; f4.name = "Pepperoni Pizza"; f4.price = 8.99; f4.rating = 4.8; f4.restaurantId = 2;
        service.foodMenu.insert(f4);

        FoodItem f5 = new FoodItem();
        f5.id = 5; f5.name = "Garlic Bread";   f5.price = 3.49; f5.rating = 4.3; f5.restaurantId = 2;
        service.foodMenu.insert(f5);

        FoodItem f6 = new FoodItem();
        f6.id = 6; f6.name = "Pasta Alfredo";  f6.price = 9.99; f6.rating = 4.7; f6.restaurantId = 2;
        service.foodMenu.insert(f6);

        Customer c1 = new Customer();
        c1.id = 1; c1.name = "Rahim"; c1.phone = "01710000000"; c1.feedback = null;
        service.addCustomer(c1);

        Customer c2 = new Customer();
        c2.id = 2; c2.name = "Karim"; c2.phone = "01820000000"; c2.feedback = null;
        service.addCustomer(c2);

        System.out.println("Sample data loaded.\n");
    }

    // =========================================================================
    // HANDLERS
    // =========================================================================

    static void handleShowRestaurantMenu() {
        int id = readInt("Enter Restaurant ID: ");
        service.showMenuByRestaurant(id);
    }

    // BUG 4 FIX: collect phone number here and pass it to placeOrder
    static void handlePlaceOrder() {
        System.out.println("\n--- Place Order ---");
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        // If this is a new customer, ask for phone now
        String phone = "N/A";
        if (service.customers.findByNameSilent(name) == null) {
            System.out.print("Enter your phone number: ");
            phone = sc.nextLine();
        }

        int restaurantId = readInt("Enter Restaurant ID to order from: ");
        service.showMenuByRestaurant(restaurantId);

        System.out.print("Enter items, comma separated (e.g. Cheeseburger, French Fries): ");
        String items = sc.nextLine();

        // Use the overloaded version that takes phone number
        service.placeOrder(name, phone, items, restaurantId);
    }

    static void handleSearchMenuItem() {
        System.out.print("Enter item name to search: ");
        service.searchMenuItem(sc.nextLine());
    }

    // BUG 2 FIX: separate option to add restaurant BEFORE adding food items
    static void handleAddRestaurant() {
        Restaurant r = new Restaurant();
        r.id = readInt("Enter Restaurant ID: ");
        System.out.print("Enter Restaurant name: ");
        r.name = sc.nextLine();
        System.out.print("Enter location: ");
        r.location = sc.nextLine();
        service.addRestaurant(r);
        System.out.println("Restaurant \"" + r.name + "\" added. You can now add food items to it (option 12).");
    }

    static void handleAddFoodItem() {
        System.out.println("(Use option 2 to see existing restaurant IDs first)");
        FoodItem f = new FoodItem();
        f.id           = readInt("Enter food ID: ");
        System.out.print("Enter food name: ");
        f.name         = sc.nextLine();
        f.price        = readDouble("Enter price: $");
        f.rating       = readDouble("Enter rating (0-5): ");
        f.restaurantId = readInt("Enter Restaurant ID this item belongs to: ");
        service.addFoodItem(f);
    }

    static void handleShowCustomerDetails() {
        System.out.print("Enter customer name: ");
        service.showCustomerDetails(sc.nextLine());
    }

    static void handleAssignDeliveryPerson() {
        int id = readInt("Enter Order ID: ");
        System.out.print("Enter delivery person name: ");
        service.assignDeliveryPerson(id, sc.nextLine());
    }

    static void handleEstimatedTime() {
        int id = readInt("Enter Order ID: ");
        System.out.println("Est. delivery time: " + service.estimatedDeliveryTime(id) + " minutes");
    }

    static void handleSaveFeedback() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter feedback: ");
        service.saveFeedback(name, sc.nextLine());
    }

    // BUG 1 FIX: takes customer name + coupon, writes result back to Customer object
    static void handleApplyDiscount() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter coupon code (SAVE10 / SAVE20): ");
        String coupon = sc.nextLine();
        service.applyDiscount(name, coupon);
    }

    // BUG 3 FIX: asks WHICH customer's receipt instead of always showing lastOrder
    static void handleGenerateReceipt() {
        System.out.print("Enter customer name for receipt: ");
        String name = sc.nextLine();
        service.generateReceipt(name);
    }

    // =========================================================================
    // INPUT HELPERS
    // =========================================================================
    static int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(sc.nextLine().trim());
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(sc.nextLine().trim());
    }
}
