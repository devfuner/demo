package com.example.demo.sample;

public class TrackingInfo {

    private String invoiceNo;
    private String receiverName;
    private String itemName;
    private String productInfo;
    private String senderName;
    private String receiverAddr;
    private String orderNumber;

public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "TrackingInfo{" +
                "invoiceNo=" + invoiceNo +
                ", receiverName='" + receiverName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverAddr='" + receiverAddr + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
