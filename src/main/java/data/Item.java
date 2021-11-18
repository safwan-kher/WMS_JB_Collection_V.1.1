package main.java.data;

import java.util.Date;

/**
 * This stores details of an Item
 *
 * @author riteshp
 */
public class Item {
  /** Current state of the item */
  private String state;
  /** Category of the item */
  private String category;
  /** The warehouse ID */
  private int warehouse;
  /** The date when this item was added to the warehouse */
  private Date dateOfStock;

  // setters and getters
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(int warehouse) {
    this.warehouse = warehouse;
  }

  public Date getDateOfStock() {
    return dateOfStock;
  }

  public void setDateOfStock(Date dateOfStock) {
    this.dateOfStock = dateOfStock;
  }

  /**
   * Textual representation of the item. It's a combination of the state followed by the category
   */
  @Override
  public String toString() {
    return String.format("%s %s", this.getState(), this.getCategory().toLowerCase());
  }
}
