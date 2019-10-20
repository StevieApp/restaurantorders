package com.example.restaurantorders;

import java.util.ArrayList;

public class Order {
    private int id;
    private int user_id;
    private int country_code;
    private int mobile;
    private int total;
    private int dispatch_status;
    private int delivery_reference;
    private int delivery_contact_phone_number;
    private int delivery_charge;
    private int payment_details_amount;
    private String name,
            email,
            error,
            delivery_address,
            delivery_drop_off_coordinate,
            delivery_locality,
            delivery_country,
            payment_details_type,
            payment_details_reference,
            payment_details_processor_reference,
            payment_details_status,
            payment_details_phone_number,
            dispatch_time,
            created_at,
            updated_at;

    public ArrayList<OrderItem> cart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDispatch_status() {
        return dispatch_status;
    }

    public void setDispatch_status(int dispatch_status) {
        this.dispatch_status = dispatch_status;
    }

    public int getDelivery_reference() {
        return delivery_reference;
    }

    public void setDelivery_reference(int delivery_reference) {
        this.delivery_reference = delivery_reference;
    }

    public int getDelivery_contact_phone_number() {
        return delivery_contact_phone_number;
    }

    public void setDelivery_contact_phone_number(int delivery_contact_phone_number) {
        this.delivery_contact_phone_number = delivery_contact_phone_number;
    }

    public int getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(int delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public int getPayment_details_amount() {
        return payment_details_amount;
    }

    public void setPayment_details_amount(int payment_details_amount) {
        this.payment_details_amount = payment_details_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_drop_off_coordinate() {
        return delivery_drop_off_coordinate;
    }

    public void setDelivery_drop_off_coordinate(String delivery_drop_off_coordinate) {
        this.delivery_drop_off_coordinate = delivery_drop_off_coordinate;
    }

    public String getDelivery_locality() {
        return delivery_locality;
    }

    public void setDelivery_locality(String delivery_locality) {
        this.delivery_locality = delivery_locality;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public String getPayment_details_type() {
        return payment_details_type;
    }

    public void setPayment_details_type(String payment_details_type) {
        this.payment_details_type = payment_details_type;
    }

    public String getPayment_details_reference() {
        return payment_details_reference;
    }

    public void setPayment_details_reference(String payment_details_reference) {
        this.payment_details_reference = payment_details_reference;
    }

    public String getPayment_details_processor_reference() {
        return payment_details_processor_reference;
    }

    public void setPayment_details_processor_reference(String payment_details_processor_reference) {
        this.payment_details_processor_reference = payment_details_processor_reference;
    }

    public String getPayment_details_status() {
        return payment_details_status;
    }

    public void setPayment_details_status(String payment_details_status) {
        this.payment_details_status = payment_details_status;
    }

    public String getPayment_details_phone_number() {
        return payment_details_phone_number;
    }

    public void setPayment_details_phone_number(String payment_details_phone_number) {
        this.payment_details_phone_number = payment_details_phone_number;
    }

    public String getDispatch_time() {
        return dispatch_time;
    }

    public void setDispatch_time(String dispatch_time) {
        this.dispatch_time = dispatch_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<OrderItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<OrderItem> cart) {
        this.cart = cart;
    }
}
