package com.example.a2family.Classes;

import java.util.Calendar;
import java.util.Objects;

public class Product {
    private String description;
    private int quantity;
    private String unique;

    public Product() {
    }

    public Product(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
        this.unique = String.valueOf(Calendar.getInstance().getTimeInMillis()+ quantity)+""+description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    @Override
    public String toString() {
        return ""+description +"    Quantita: "+quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getQuantity() == product.getQuantity() && this.description.equals(((Product) o).getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getQuantity());
    }
}
