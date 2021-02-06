package com.example.vendor;

public class ContentData {

    private String itemName, itemImage, itemPriceInGold, itemPriceInSilver, itemPriceInFix, itemPriceInGuest, itemPriceInBronze, itemCategory, isKgs;


    public ContentData() {
    }




    public ContentData(String itemName, String itemImage,String itemPriceInGuest,String itemCategory,String itemPriceInFix ,String itemPriceInGold, String itemPriceInSilver, String itemPriceInBronze, String temPriceInFix, String temPriceInGuest, String isKgs) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemCategory = itemCategory;
        this.itemPriceInGold = itemPriceInGold;
        this.itemPriceInSilver = itemPriceInSilver;
        this.itemPriceInBronze = itemPriceInBronze;
        this.itemPriceInFix = itemPriceInFix;
        this.itemPriceInGuest = itemPriceInGuest;
        this.isKgs = isKgs;

    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemPriceInGold() {
        return itemPriceInGold;
    }

    public void setItemPriceInGold(String itemPriceInGold) {
        this.itemPriceInGold = itemPriceInGold;
    }

    public String getItemPriceInSilver() {
        return itemPriceInSilver;
    }

    public void setItemPriceInSilver(String itemPriceInSilver) {
        this.itemPriceInSilver = itemPriceInSilver;
    }

    public String getItemPriceInFix() {
        return itemPriceInFix;
    }

    public void setItemPriceInFix(String itemPriceInFix) {
        this.itemPriceInFix = itemPriceInFix;
    }

    public String getItemPriceInGuest() {
        return itemPriceInGuest;
    }

    public void setItemPriceInGuest(String itemPriceInGuest) {
        this.itemPriceInGuest = itemPriceInGuest;
    }

    public String getItemPriceInBronze() {
        return itemPriceInBronze;
    }

    public void setItemPriceInBronze(String itemPriceInBronze) {
        this.itemPriceInBronze = itemPriceInBronze;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getIsKgs() {
        return isKgs;
    }

    public void setIsKgs(String isKgs) {
        this.isKgs = isKgs;
    }

    @Override
    public String toString() {
        return "ContentData{" +
                "itemName='" + itemName + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemPriceInGold='" + itemPriceInGold + '\'' +
                ", itemPriceInSilver='" + itemPriceInSilver + '\'' +
                ", itemPriceInFix='" + itemPriceInFix + '\'' +
                ", itemPriceInGuest='" + itemPriceInGuest + '\'' +
                ", itemPriceInBronze='" + itemPriceInBronze + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", isKgs='" + isKgs + '\'' +
                '}';
    }
}
