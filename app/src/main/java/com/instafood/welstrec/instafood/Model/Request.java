package com.instafood.welstrec.instafood.Model;

import java.util.List;

public class Request {
    private String zona;
    private String name;
    private String address;
    private String total;
    private List<Order> foods;



    private String metodoPago;

    public Request() {
    }

    public Request(String zona, String name, String address, String total, List<Order> foods,String metodo) {
        this.zona = zona;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.metodoPago=metodo;
    }

    public String getPhone() {
        return zona;
    }

    public void setPhone(String phone) {
        this.zona = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
