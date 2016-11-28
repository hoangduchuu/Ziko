package vn.zikoteam.ziko.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dk-darkness on 21/11/2016.
 */

@IgnoreExtraProperties
public class User {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String avatar;
    private String address;

    public User() {
    }

    public User(String id, String name, String phoneNumber, String email, String avatar, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
        this.address = address;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("name", getName());
        result.put("phoneNumber", getPhoneNumber());
        result.put("email", getEmail());
        result.put("avatar", getAvatar());
        result.put("address", getAddress());

        return result;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }
}
