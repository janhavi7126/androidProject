package com.example.android.mymall.Activities;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {
    public static  final int CART_ITEM = 0;
    public static  final int TOTALAMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /////////////////////
    private String productID;
    private String productImage;
    private String productTitle;
    private Long freeCoupens;
    private Long productQuantity;
    private Long maxQuantity;
    private Long stockQuantity;
    private String productPrice;
    private String cuttedPrice;
    private Long offersApplied;
    private Long coupensApplied;
    private boolean inStock;
    private List<String> qtyIDs;
    private boolean qtyError;
    private String selectedCouponId;
    private String discountedPrice;
    private boolean Cod;

    public CartItemModel(int type, String productID, String productImage, String productTitle, Long freeCoupens, Long productQuantity, String productPrice, String cuttedPrice, Long offersApplied, Long coupensApplied,boolean inStock,Long maxQuantity,Long stockQuantity,boolean Cod) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.offersApplied = offersApplied;
        this.coupensApplied = coupensApplied;
        this.inStock = inStock;
        this.maxQuantity=maxQuantity;
        this.stockQuantity = stockQuantity;
        qtyIDs = new ArrayList<>();
        qtyError = false;
        this.Cod = Cod;

    }

    public boolean isCod() {
        return Cod;
    }

    public void setCod(boolean cod) {
        Cod = cod;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCouponId() {
        return selectedCouponId;
    }

    public void setSelectedCouponId(String selectedCouponId) {
        this.selectedCouponId = selectedCouponId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(Long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public Long getCoupensApplied() {
        return coupensApplied;
    }

    public void setCoupensApplied(Long coupensApplied) {
        this.coupensApplied = coupensApplied;
    }

    //////////////////////////////////////////////////////

    private int noOfItems;
    private int totalItemsAmount;
    private String deliveryAmount;
    private int savedAmount;
    private int overAllTotalAmount;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }

    public int getTotalItemsAmount() {
        return totalItemsAmount;
    }

    public void setTotalItemsAmount(int totalItemsAmount) {
        this.totalItemsAmount = totalItemsAmount;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public int getOverAllTotalAmount() {
        return overAllTotalAmount;
    }

    public void setOverAllTotalAmount(int overAllTotalAmount) {
        this.overAllTotalAmount = overAllTotalAmount;
    }
}
