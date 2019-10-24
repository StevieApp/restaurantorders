package com.kilimanjarofood.restaurantorders;

public class OrderItem {
    private int product_id,
            quantity, price,
            accompaniment_id,
            product_attrubute_price;

    private String product_name,
            description,
            products_attribute_accompaniment,
            product_attrubute_size;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAccompaniment_id() {
        return accompaniment_id;
    }

    public void setAccompaniment_id(int accompaniment_id) {
        this.accompaniment_id = accompaniment_id;
    }

    public int getProduct_attrubute_price() {
        return product_attrubute_price;
    }

    public void setProduct_attrubute_price(int product_attrubute_price) {
        this.product_attrubute_price = product_attrubute_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducts_attribute_accompaniment() {
        return products_attribute_accompaniment;
    }

    public void setProducts_attribute_accompaniment(String products_attribute_accompaniment) {
        this.products_attribute_accompaniment = products_attribute_accompaniment;
    }

    public String getProduct_attrubute_size() {
        return product_attrubute_size;
    }

    public void setProduct_attrubute_size(String product_attrubute_size) {
        this.product_attrubute_size = product_attrubute_size;
    }
}
