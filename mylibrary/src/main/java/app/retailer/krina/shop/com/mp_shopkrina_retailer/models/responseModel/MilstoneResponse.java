package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

public class MilstoneResponse {
    private String M_Id;
    private String rPoint;
    private String mPoint;
    private String active;

    public String getM_Id() {
        return M_Id;
    }

    public void setM_Id(String m_Id) {
        M_Id = m_Id;
    }

    public String getrPoint() {
        return rPoint;
    }

    public void setrPoint(String rPoint) {
        this.rPoint = rPoint;
    }

    public String getmPoint() {
        return mPoint;
    }

    public void setmPoint(String mPoint) {
        this.mPoint = mPoint;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
