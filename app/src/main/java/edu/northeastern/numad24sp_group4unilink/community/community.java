package edu.northeastern.numad24sp_group4unilink.community;

public class community {

    private String id;

    private String tag;
    private String[] users;
    private String pictureUrl;

    public community() {
        // Default constructor required for Firebase
    }

    public community(String id, String tag, String[] users, String pictureUrl) {
        this.id=id;
        this.tag = tag;
        this.users = users;
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}


