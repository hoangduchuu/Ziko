package vn.zikoteam.ziko.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dk-darkness on 30/11/2016.
 */
@IgnoreExtraProperties
public class Order {
    private String idOrder;
    private String idFood;
    private String imgFood;
    private String namFood;
    private String addressFood;
    private String priceFood;
    private String idKh;
    private String nameKh;
    private String phoneNumberKh;
    private String addressKh;
    private int review;
    private String connentReview;
    private String idShipper;
    private String avatarShipper;
    private String nameShipper;
    private int status;

    public Order() {
    }

    public Order(String idOrder, String idFood, String imgFood, String namFood, String addressFood, String priceFood, String idKh, String nameKh, String phoneNumberKh, String addressKh, int review, String connentReview, String idShipper, String avatarShipper, String nameShipper, int status) {
        this.idOrder = idOrder;
        this.idFood = idFood;
        this.imgFood = imgFood;
        this.namFood = namFood;
        this.addressFood = addressFood;
        this.priceFood = priceFood;
        this.idKh = idKh;
        this.nameKh = nameKh;
        this.phoneNumberKh = phoneNumberKh;
        this.addressKh = addressKh;
        this.review = review;
        this.connentReview = connentReview;
        this.idShipper = idShipper;
        this.avatarShipper = avatarShipper;
        this.nameShipper = nameShipper;
        this.status = status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idOrder", getIdOrder());
        result.put("idFood", getIdFood());
        result.put("imgFood", getImgFood());
        result.put("namFood", getNamFood());
        result.put("addressFood", getAddressFood());
        result.put("priceFood", getPriceFood());
        result.put("idKh", getIdKh());
        result.put("nameKh", getNameKh());
        result.put("phoneNumberKh", getPhoneNumberKh());
        result.put("addressKh", getAddressKh());
        result.put("review", getReview());
        result.put("connentReview", getConnentReview());
        result.put("idShipper", getIdShipper());
        result.put("avatarShipper", getAvatarShipper());
        result.put("nameShipper", getNameShipper());
        result.put("status", getStatus());

        return result;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public String getIdFood() {
        return idFood;
    }

    public String getImgFood() {
        return imgFood;
    }

    public String getNamFood() {
        return namFood;
    }

    public String getAddressFood() {
        return addressFood;
    }

    public String getPriceFood() {
        return priceFood;
    }

    public String getIdKh() {
        return idKh;
    }

    public String getNameKh() {
        return nameKh;
    }

    public String getPhoneNumberKh() {
        return phoneNumberKh;
    }

    public String getAddressKh() {
        return addressKh;
    }

    public int getReview() {
        return review;
    }

    public String getConnentReview() {
        return connentReview;
    }

    public String getIdShipper() {
        return idShipper;
    }

    public String getAvatarShipper() {
        return avatarShipper;
    }

    public String getNameShipper() {
        return nameShipper;
    }

    public int getStatus() {
        return status;
    }
}
