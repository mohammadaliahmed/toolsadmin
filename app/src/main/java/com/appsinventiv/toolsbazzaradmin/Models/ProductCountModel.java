package com.appsinventiv.toolsbazzaradmin.Models;

/**
 * Created by AliAh on 20/06/2018.
 */

public class ProductCountModel {
    Product product;
    int quantity;
    long time;

    public ProductCountModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCountModel model = (ProductCountModel) o;
        return product != null ? product.equals(model.product) : model.product == null;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;

        return result;
    }

    public ProductCountModel(Product product, int quantity, long time) {
        this.product = product;
        this.quantity = quantity;
        this.time = time;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
