package vn.zikoteam.ziko.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dk-darkness on 24/11/2016.
 */

@IgnoreExtraProperties
public class Food {
    private String nameFood;
    private String priceFood;
    private String addressFood;
    private String imageFood;
    private String talkAboutFood;
    private String idUser;
    private String userName;
    private String userAvatar;
    private int favoriteFood;

    public Food() {
    }

    public Food(String nameFood, String priceFood, String addressFood,
                String imageFood, String talkAboutFood, String idUser,
                String userName, String userAvatar, int favoriteFood) {
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.addressFood = addressFood;
        this.imageFood = imageFood;
        this.talkAboutFood = talkAboutFood;
        this.idUser = idUser;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.favoriteFood = favoriteFood;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nameFood", getNameFood());
        result.put("priceFood", getPriceFood());
        result.put("addressFood", getAddressFood());
        result.put("imageFood", getImageFood());
        result.put("talkAboutFood", getTalkAboutFood());
        result.put("idUser", getIdUser());
        result.put("userName", getUserName());
        result.put("userAvatar", getUserAvatar());
        result.put("favoriteFood", getFavoriteFood());

        return result;
    }

    public String getNameFood() {
        return nameFood;
    }

    public String getPriceFood() {
        return priceFood;
    }

    public String getAddressFood() {
        return addressFood;
    }

    public String getImageFood() {
        return imageFood;
    }

    public String getTalkAboutFood() {
        return talkAboutFood;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public int getFavoriteFood() {
        return favoriteFood;
    }
}
