package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

import android.text.Html;

import java.text.DecimalFormat;

public class PendingPaymentModel {
    String ID ;
    String Date ;
    String Particulars;
    String LagerID ;
    String VouchersTypeID;
    String VouchersNo ;
    double Debit ;
    String Credit;
    String ObjectID ;
    String ObjectType ;
    String AffectedLadgerID ;
    String Active ;
    String CreatedBy ;
    String CreatedDate ;
    String CreditString ;
    String DebitString ;
    String LadgerName ;
    String VoucherName ;

    public PendingPaymentModel(String ID, String date, String particulars, String lagerID, String vouchersTypeID, String vouchersNo, double debit, String credit, String objectID, String objectType, String affectedLadgerID, String active, String createdBy, String createdDate, String creditString, String debitString, String ladgerName, String voucherName) {
        this.ID = ID;
        Date = date;
        Particulars = particulars;
        LagerID = lagerID;
        VouchersTypeID = vouchersTypeID;
        VouchersNo = vouchersNo;
        Debit = debit;
        Credit = credit;
        ObjectID = objectID;
        ObjectType = objectType;
        AffectedLadgerID = affectedLadgerID;
        Active = active;
        CreatedBy = createdBy;
        CreatedDate = createdDate;
        CreditString = creditString;
        DebitString = debitString;
        LadgerName = ladgerName;
        VoucherName = voucherName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public String getLagerID() {
        return LagerID;
    }

    public void setLagerID(String lagerID) {
        LagerID = lagerID;
    }

    public String getVouchersTypeID() {
        return VouchersTypeID;
    }

    public void setVouchersTypeID(String vouchersTypeID) {
        VouchersTypeID = vouchersTypeID;
    }

    public String getVouchersNo() {
        return VouchersNo;
    }

    public void setVouchersNo(String vouchersNo) {
        VouchersNo = vouchersNo;
    }

    public String getDebit() {
        return String.valueOf(Html.fromHtml("<font color=#FF4500>&#8377;" + new DecimalFormat("#,###.##").format(Debit)));
    }

    public void setDebit(double debit) {
        Debit = debit;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getAffectedLadgerID() {
        return AffectedLadgerID;
    }

    public void setAffectedLadgerID(String affectedLadgerID) {
        AffectedLadgerID = affectedLadgerID;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreditString() {
        return CreditString;
    }

    public void setCreditString(String creditString) {
        CreditString = creditString;
    }

    public String getDebitString() {
        return new DecimalFormat("##.##").format(Double.parseDouble(DebitString));
    }

    public void setDebitString(String debitString) {
        DebitString = debitString;
    }

    public String getLadgerName() {
        return LadgerName;
    }

    public void setLadgerName(String ladgerName) {
        LadgerName = ladgerName;
    }

    public String getVoucherName() {
        return VoucherName;
    }

    public void setVoucherName(String voucherName) {
        VoucherName = voucherName;
    }
}
