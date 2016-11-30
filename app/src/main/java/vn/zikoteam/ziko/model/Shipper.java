package vn.zikoteam.ziko.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dk-darkness on 30/11/2016.
 */

@IgnoreExtraProperties
public class Shipper implements Serializable {
    private String shipperID;
    private String name;
    private String email;
    private String dateOfBirth;
    private String address;
    private int countLike;
    private int countDisLike;
    private String phoneNumber;
    private String avatar;

    public Shipper() {
    }

    public Shipper(String shipperID, String name, String email
            , String dateOfBirth, String address, int countLike
            , int countDisLike, String phoneNumber, String avatar) {
        this.shipperID = shipperID;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.countLike = countLike;
        this.countDisLike = countDisLike;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getShipperID());
        result.put("name", getName());
        result.put("phonenumber", getPhoneNumber());
        result.put("email", getEmail());
        result.put("avatar", getAvatar());
        result.put("address", getAddress());
        result.put("like", getCountLike());
        result.put("dislike", getCountDisLike());
        result.put("dateofbirth", getDateOfBirth());

        return result;
    }

    public String getShipperID() {
        return shipperID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public int getCountLike() {
        return countLike;
    }

    public int getCountDisLike() {
        return countDisLike;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }
}
