package com.platzi.model;

public class Gato {

    private String id;
    private String url;
    private String image;
    private static final String apiKey = ApiKey.getApiKey();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static String getApiKey() {
        return apiKey;
    }

}
