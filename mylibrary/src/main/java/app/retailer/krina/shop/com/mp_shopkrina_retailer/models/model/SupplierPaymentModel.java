package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

public class SupplierPaymentModel {

    private int CustomerId;
    private String FromDate;
    private String ToDate;
    private int LedgerTypeID;
    private boolean IsGenerateExcel;
    private String ReportCode;

    public SupplierPaymentModel(int CustomerId, String fromDate, String toDate, int ledgerTypeID, boolean isGenerateExcel, String reportCode) {
        this.CustomerId = CustomerId;
        this.FromDate = fromDate;
        this.ToDate = toDate;
        this.LedgerTypeID = ledgerTypeID;
        this.IsGenerateExcel = isGenerateExcel;
        this.ReportCode = reportCode;
    }

    public int getSupplierID() {
        return CustomerId;
    }

    public void setSupplierID(int supplierID) {
        CustomerId = supplierID;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public int getLedgerTypeID() {
        return LedgerTypeID;
    }

    public void setLedgerTypeID(int ledgerTypeID) {
        LedgerTypeID = ledgerTypeID;
    }

    public boolean isGenerateExcel() {
        return IsGenerateExcel;
    }

    public void setGenerateExcel(boolean generateExcel) {
        IsGenerateExcel = generateExcel;
    }

    public String getReportCode() {
        return ReportCode;
    }

    public void setReportCode(String reportCode) {
        ReportCode = reportCode;
    }
}
