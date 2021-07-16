package com.instafood.welstrec.instafood.Model;

public class Order {
    private String ProductId;
    private String ProductName;
    private String Quatity;
    private String Price;
    private String Discount;

    public Order(String productId) {
    }

    public Order(String productId, String productName, String quatity, String price, String discount) {
        ProductId = productId;
        ProductName = productName;
        Quatity = quatity;
        Price = price;
        Discount = discount;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuatity() {
        return Quatity;
    }

    public void setQuatity(String quatity) {
        Quatity = quatity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
