package com.appsinventiv.toolsbazzaradmin.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AliAh on 20/06/2018.
 */

public class Product {
    String id, title, subtitle, isActive, thumbnailUrl, mainCategory, subCategory;
    long time;
    float costPrice, wholeSalePrice, retailPrice;
    long minOrderQuantity;
    String measurement;
    VendorModel vendor;
    int sku;
    String sellingTo;
    String description;
    List<String> colorList;
    List<String> sizeList;
    float oldWholeSalePrice, oldRetailPrice;
    float rating;
    ArrayList<String> pictures;
    ArrayList<String> category;
    int quantityAvailable;
    String brandName,productContents,warrantyType,productWeight;
    String dimen;
    String sellerProductStatus;
    String uploadedBy;

    public Product() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        if (id != null && product.id != null) {
            if (id.equalsIgnoreCase(product.id)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;

        }
//        return id != null ? !id.equals(product.id) : product.id != null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Product(String id, String title, String subtitle, String isActive,
                   int sku, String thumbnailUrl, String mainCategory, String subCategory,
                   long time, float costPrice, float wholeSalePrice, float retailPrice,
                   long minOrderQuantity, String measurement, VendorModel vendor, String sellingTo,
                   String description,

                   List<String> sizeList,
                   List<String> colorList,
                   float oldWholeSalePrice, float oldRetailPrice
            , float rating,
                   ArrayList<String> category,int quantityAvailable,
                   String brandName, String productContents,String warrantyType,String productWeight,
                   String dimen,String uploadedBy,String sellerProductStatus

    ) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.isActive = isActive;
        this.sku = sku;
        this.thumbnailUrl = thumbnailUrl;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.time = time;
        this.costPrice = costPrice;
        this.wholeSalePrice = wholeSalePrice;
        this.retailPrice = retailPrice;
        this.minOrderQuantity = minOrderQuantity;
        this.measurement = measurement;
        this.vendor = vendor;
        this.sellingTo = sellingTo;
        this.description = description;
        this.sizeList = sizeList;
        this.colorList = colorList;
        this.oldRetailPrice = oldRetailPrice;
        this.oldWholeSalePrice = oldWholeSalePrice;
        this.rating = rating;
        this.category = category;
        this.quantityAvailable=quantityAvailable;
        this.brandName=brandName;
        this.productContents=productContents;
        this.warrantyType=warrantyType;
        this.productWeight=productWeight;
        this.dimen=dimen;
        this.uploadedBy=uploadedBy;
        this.sellerProductStatus=sellerProductStatus;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getSellerProductStatus() {
        return sellerProductStatus;
    }

    public void setSellerProductStatus(String sellerProductStatus) {
        this.sellerProductStatus = sellerProductStatus;
    }

    public String getDimen() {
        return dimen;
    }

    public void setDimen(String dimen) {
        this.dimen = dimen;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductContents() {
        return productContents;
    }

    public void setProductContents(String productContents) {
        this.productContents = productContents;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public List<String> getColorList() {
        return colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }

    public List<String> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<String> sizeList) {
        this.sizeList = sizeList;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public float getOldWholeSalePrice() {
        return oldWholeSalePrice;
    }

    public void setOldWholeSalePrice(float oldWholeSalePrice) {
        this.oldWholeSalePrice = oldWholeSalePrice;
    }

    public float getOldRetailPrice() {
        return oldRetailPrice;
    }

    public void setOldRetailPrice(float oldRetailPrice) {
        this.oldRetailPrice = oldRetailPrice;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getSellingTo() {
        return sellingTo;
    }

    public void setSellingTo(String sellingTo) {
        this.sellingTo = sellingTo;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(float costPrice) {
        this.costPrice = costPrice;
    }

    public float getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(float wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }

    public long getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(long minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }


}
