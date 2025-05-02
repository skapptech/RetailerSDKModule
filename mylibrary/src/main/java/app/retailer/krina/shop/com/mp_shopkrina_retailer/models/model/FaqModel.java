package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

public class FaqModel {
    private  String Id;
    private  String title;
    private  String videoUrl;

    public FaqModel(String id, String title, String videoUrl) {
        Id = id;
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
