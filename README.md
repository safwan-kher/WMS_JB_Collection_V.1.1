# Collections

## Topics covered

Lists, dictionaries and other collection types. Working with dates and texts.

## Goal achieved

By the end of the exercise you will have upgraded your query command line tool to use a more complex
data set and perform additional operations.

More specifically, you will add the following features:

- Add a total counter when listing all items.
- Change the search to case-insensitive.
- The search will list all items matching, instead of indicating the number. For each item, it will
  also show the number of days it has been in stock.
- Add a new menu option to browse the items by category.

## Data

Copy the file `sample/data.json` to `src/main/resources`. Mark the `resources` folder as
a `Resources Root` on Intellij, if it isn't already.

This file has a little more data and uses a list of dictionaries to describe the data. Each
dictionary has the following keys:

- **state** *string*: The condition of the item (example: Brand new, old, cheap,...)
- **category** *string*: The type of item (example: Laptop, Tablet, Headphones,...)
- **warehouse** *integer*: The warehouse number where this item is located.
- **date_of_stock** *date*: The date and time the item was stocked.

The full name of the item will be a composition of `state` and `category`.

Make sure `src/main/java` is marked as a **Sources Root** folder. Copy the `Item` and
the `Repository` classes as well into your project. the `Repository` class should replace the old
one.

The `Item` class is a **model class** that represents an item from the `data.json`
file.

The new `Repository` class contains a **static block code** that loads every entry from `data.json`,
model it into the `Item` class and then populate the `List<Item> ITEM_LIST` with the items.

> **Hint:** If you're getting an error on the `Reposity` class due to the classes from
> `org.json.simple`, make sure the `json-simple-1.1.1.jar` file is present on your **classpath**.

## The Repository

The `Repository` class contains a couple methods that should be implemented. These methods will be
used along the changes that needs to be done in your project.

## Description

### Refactoring

After replacing the data, you will have to edit the `TheWarehouseManager` class and the first thing
you will have to do is changing the `listItemsByWarehouse()` method.

It should use the `Repository.getWarehouses()` method, which should return a `Set<Integer>`. This
set represents a set of IDs of each warehouse.

With those IDs, you should call the method `Repository.getItemsByWarehouse(warehouse)` on each to
retrieve a `List<Item>`. Finally, you should print each item.

> Notice that the full name of each item is now a composition of the keys `state` and `category`. To replicate the current behavior, the script must compare the user input with the combination of `state` and `category`.

Once all the current features are working as expected with the new data, add all those changes to
the remote repository (add them locally, commit them with a meaningful text and push them).

After that, add the following features/changes.

> **IMPORTANT**
>
> Do not forget to push all the changes to the remote repository again at the end of the exercise. Commit the changes with a meaningful description.

### Changes on menu option 1 (list all items)

When printing the list of items, and at the end of the whole list, print the total amount of items
in stock on each warehouse:

##### Sample output:

```
...
- High quality Tablet

Total items in warehouse 1: ???
Total items in warehouse 2: ???

Thank you for your visit, Wolfgang!
```

### Changes on menu option 2 (search and place order)

Change the search of the items (operation number `2`) so that:

1. It produces the same result when typing `cheap tablet` and `CHEAP TABLET` (the search should
   be **case-insensitive**).
1. If the search returns at least one result (in any warehouse), it prints a list of all the items
   showing the name of the warehouse and the number of days it has passed since they were stocked.
2. It still prints the maximum availability only when the item is found in more than one warehouse.
3. It still prints `Location: Not in stock` when the item is not found.

##### Sample outputs:

```
...
What is the name of the item? funny headphones
Amount available: 5
Location:
- Warehouse 1 (in stock for 905 days)
- Warehouse 1 (in stock for 33 days)
- Warehouse 1 (in stock for 193 days)
- Warehouse 2 (in stock for 957 days)
- Warehouse 2 (in stock for 188 days)
Maximum availability: 3 in Warehouse 1

Would you like to order this item?(y/n) n
...
```

```
...
What is the name of the item? bigger headphones
Amount available: 0
Location: Not in stock
...
```

### New menu option (browse by category)

Add a new menu option named `Browse by category`. Assign it the number 3 and move down the `Quit`
option to number 4. When selected, this option should:

1. Show a menu of available categories. This menu will have to include a numeric code (the number
   the user will type in to select a category), the name of the category and the amount of items
   available in that category (in any warehouse).

   > There is no list of categories in the dataset, so you will have to iterate all the stock to identify the categories and count their items.
   >
   > You will also have to find a way to produce a numeric identifier for each category.
   >
   > The menu list should show single categories (each category should only appear once).

1. Ask the user to type the category number of their choice.
1. List all items in that category. This time, print them one after the other (not separated by
   warehouse) and include the name of the warehouse at the end of each line.

   > Be aware that the code associated to each category will be an auto-generated numeric code and the items have a text value as category. You will have to think of a way to identify each number typed by the user to the correct category name to be able to filter the stock.

##### Sample output:

```
What is your user name? Example

Hello, Example!
What would you like to do?
1. List items by warehouse
2. Search an item and place an order
3. Browse by category
4. Quit
Type the number of the operation: 3

1. Keyboard (34)
2. Smartphone (39)
3. Mouse (39)
4. Laptop (40)
5. Headphones (37)
6. Monitor (40)
7. Router (30)
8. Tablet (41)
Type the number of the category to browse: 4

List of laptops available:
Exceptional laptop, Warehouse 2
...
Second hand laptop, Warehouse 1

Thank you for your visit, Example!
```
