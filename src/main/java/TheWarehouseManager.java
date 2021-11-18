package main.java;

import main.java.data.Item;
import main.java.data.Repository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager {
  // =====================================================================================
  // Member Variables
  // =====================================================================================

  // To read inputs from the console/CLI
  private final Scanner reader = new Scanner(System.in);
  private final String[] userOptions = {
    "1. List items by warehouse",
    "2. Search an item and place an order",
    "3. Browse by category",
    "4. Quit"
  };
  // To refer the user provided name.
  private String userName;

  // =====================================================================================
  // Public Member Methods
  // =====================================================================================

  /** Welcome User */
  public void welcomeUser() {
    this.seekUserName();
    this.greetUser();
  }

  /** Ask for user's choice of action */
  public int getUsersChoice() {
    int choice;
    // List all options
    System.out.println("What would you like to do?");
    for (String option : this.userOptions) {
      System.out.println(option);
    }

    System.out.print("Type the number of the operation: ");
    // Keep asking for user's choice until a valid value is received
    do {
      String selectedOption = this.reader.nextLine();
      try {
        choice = Integer.parseInt(selectedOption);
      } catch (Exception e) {
        choice = 0;
      }
      // Guide the user to enter correct value
      if (choice < 1 || choice > userOptions.length) {
        System.out.printf("Sorry! Enter an integer between 1 and %d. ", this.userOptions.length);
      }
    } while (choice < 1 || choice > userOptions.length);

    // return the valid choice
    return choice;
  }

  /** Initiate an action based on given option */
  public void performAction(int option) {
    switch (option) {
      case 1: // "1. List items by warehouse"
        this.listItemsByWarehouse();
        break;
      case 2: // "2. Search an item and place an order"
        this.searchItemAndPlaceOrder();
        break;
      case 3: // 3. Browse by category
        this.browseByCategory();
        break;
      case 4: // "4. Quit"
        this.quit();
        break;
      default:
        System.out.println("Sorry! Invalid option.");
    }
  }

  /**
   * Confirm an action
   *
   * @return confirm
   */
  public boolean confirm(String message) {
    System.out.printf("%s (y/n)%n", message);
    String choice;
    do {
      choice = this.reader.nextLine();
      if (choice.length() > 0) {
        choice = choice.trim();
      }
    } while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

    return choice.equalsIgnoreCase("y");
  }

  /** End the application */
  public void quit() {
    System.out.printf("\nThank you for your visit, %s!\n", this.userName);
    System.exit(0);
  }

  // =====================================================================================
  // Private Methods
  // =====================================================================================

  /** Get user's name via CLI */
  private void seekUserName() {
    // Use the print method (instead of println) to not force a new line character
    // at the end
    System.out.print("What's your name? ");

    // Wait for the user input. Store the input in the member variable
    this.userName = this.reader.nextLine();
  }

  /** Print a welcome message with the given user's name */
  private void greetUser() {
    // The printf method is more efficient for a formatted output.
    System.out.printf("\nHello, %s!\n", this.userName);

    // The string concatenation is another common way.
    // System.out.println("Hello, " + this.userName + "!");
  }

  private void listItemsByWarehouse() {
    Set<Integer> warehouses = Repository.getWarehouses();
    for (int warehouse : warehouses) {
      System.out.printf("Items in warehouse %d:%n", warehouse);
      this.listItems(Repository.getItemsByWarehouse(warehouse));
    }

    System.out.println();
    for (int warehouse : warehouses) {
      System.out.printf(
          "Total items in warehouse %d: %d%n",
          warehouse, Repository.getItemsByWarehouse(warehouse).size());
    }
  }

  private void listItems(List<Item> items) {
    for (Item item : items) {
      System.out.printf("- %s%n", item.toString());
    }
  }

  private void searchItemAndPlaceOrder() {
    String itemName = askItemToOrder();
    List<Item> availableItems = this.listAvailableItems(itemName);
    if (availableItems.size() > 0) {
      this.askAmountAndConfirmOrder(availableItems.size(), itemName);
    }
  }

  private void browseByCategory() {
    Map<String, List<Item>> categoryWiseItems = new HashMap<>();
    List<String> categories = new ArrayList<>(Repository.getCategories());
    for (int i = 0; i < categories.size(); i++) {
      String category = categories.get(i);
      List<Item> catItems = Repository.getItemsByCategory(category);
      categoryWiseItems.put(category, catItems);
      System.out.printf("%d. %s (%d)%n", (i + 1), category, catItems.size());
    }

    int catIndex = 0;
    do {
      System.out.println("Type the number of the category to browse:");
      try {
        catIndex = Integer.parseInt(reader.nextLine());
      } catch (Exception e) {
        System.out.printf("Enter an integer between 1 and %d%n", categories.size());
      }

    } while (catIndex <= 0 || catIndex > categories.size());

    String category = categories.get(catIndex - 1);
    System.out.printf("List of %ss available:%n", category.toLowerCase());
    List<Item> catItems = categoryWiseItems.get(category);
    for (Item item : catItems) {
      System.out.printf("%s, Warehouse %d%n", item.toString(), item.getWarehouse());
    }
  }

  /**
   * Ask the user to specify an Item to Order
   *
   * @return String itemName
   */
  private String askItemToOrder() {
    System.out.print("What is the name of the item? ");
    return this.reader.nextLine();
  }

  /**
   * List availability of the given item
   *
   * @param itemName the item name
   * @return List<Item> availableItems
   */
  private List<Item> listAvailableItems(String itemName) {
    // find availability in Repository
    List<Item> availableItems = this.find(itemName);
    int availableCount = availableItems.size();
    System.out.printf("Amount available: %d%n", availableCount);

    if (availableCount > 0) {
      System.out.println("Location:");
      for (Item item : availableItems) {
        long noOfDaysInStock =
            TimeUnit.DAYS.convert(
                (new Date().getTime() - item.getDateOfStock().getTime()), TimeUnit.MILLISECONDS);
        System.out.printf(
            "- Warehouse %d (in stock for %d days)%n", item.getWarehouse(), noOfDaysInStock);
      }

      // get warehouse wise availability
      int maxWarehouse = 0;
      int maxAvailability = 0;
      Set<Integer> warehouses = Repository.getWarehouses();
      for (int wh : warehouses) {
        int whCount = Repository.getItemsByWarehouse(wh, availableItems).size();
        if (whCount > maxAvailability) {
          maxWarehouse = wh;
          maxAvailability = whCount;
        }
      }

      // find the warehouse with max
      System.out.printf(
          "Maximum availability: %d in warehouse %d%n", maxAvailability, maxWarehouse);
    } else {
      System.out.println("Location: Not in stock");
    }

    // return available items
    return availableItems;
  }

  /**
   * Find matching items in the repository
   *
   * @param item the item
   * @return items
   */
  private List<Item> find(String item) {
    List<Item> items = new ArrayList<>();
    for (Item wItem : Repository.getAllItems()) {
      if (wItem.toString().equalsIgnoreCase(item)) {
        items.add(wItem);
      }
    }
    return items;
  }

  /** Ask order amount and confirm order */
  private void askAmountAndConfirmOrder(int availableAmount, String item) {
    // Check if user want's to order
    boolean toOrder = this.confirm("Would you like to order this item?");
    if (toOrder) {
      // get the amount to order
      int orderAmount = this.getOrderAmount(availableAmount);
      // Confirm order if amount is positive
      if (orderAmount > 0) {
        System.out.printf(
            "%d %s %s been ordered", orderAmount, item, (orderAmount == 1 ? "has" : "have"));
      }
    }
  }

  /**
   * Get amount of order
   *
   * @param availableAmount the available amount
   * @return the order amount
   */
  private int getOrderAmount(int availableAmount) {
    int orderAmount;
    System.out.println("How many would you like?");
    do {
      String orderAmtValue = this.reader.nextLine();
      try {
        orderAmount = Integer.parseInt(orderAmtValue);
        if (orderAmount > availableAmount) {
          System.out.println("**************************************************");
          System.out.println(
              "There are not this many available. The maximum amount that can be ordered is "
                  + availableAmount);
          System.out.println("**************************************************");

          boolean orderAll = this.confirm("Would you like to order the maximum available?");
          orderAmount = orderAll ? availableAmount : 0;
        } else if (orderAmount < 0) {
          System.out.println("Please enter a value more than or equal to 0.");
        }
      } catch (Exception e) {
        orderAmount = -1;
      }
    } while (orderAmount < 0 || orderAmount > availableAmount);

    return orderAmount;
  }
}
