package com.meesmb.iprwc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Product {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    float price;

    @Column(name = "description")
    String description;

    @Column(name = "specs")
    String specs;

    @ManyToMany(targetEntity = FilterTag.class)
    Set<FilterTag> filterTags = new HashSet<>();

    @Column(name = "image_name")
    String image = "";


    public Product(String name, float price, String description, String specs, List<FilterTag> filterTags, String image) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.description = description;
        this.specs = specs;
        this.filterTags = new HashSet<>(filterTags);
        this.image = image;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Set<FilterTag> getFilterTags() {
        return filterTags;
    }

    public void setFilterTags(Set<FilterTag> filterTags) {
        this.filterTags = filterTags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
