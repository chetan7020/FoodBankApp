package com.codizcdp.foodbanksurplus.customer.model;

public class ProviderFood {
    String id, email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProviderFood(){}

    public ProviderFood(String id, String email) {
        this.id = id;
        this.email = email;
    }

}
