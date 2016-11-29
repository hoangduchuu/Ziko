package vn.zikoteam.ziko.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dk-darkness on 29/11/2016.
 */

@IgnoreExtraProperties
public class Comment {
    private String uid;
    private String userName;
    private String userAvatar;
    private String content;

    public Comment() {
    }

    public Comment(String uid, String userName, String userAvatar, String content) {
        this.uid = uid;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.content = content;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userid", getUid());
        result.put("username", getUserName());
        result.put("useravatar", getUserAvatar());
        result.put("content", getContent());

        return result;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getContent() {
        return content;
    }
}
