package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class MyIssueDetailModel implements Serializable {

    @Expose
    @SerializedName("TicketActivityLogDcs")
    private ArrayList<CommentList> TicketActivityLogDcs;
    @Expose
    @SerializedName("TATInHrs")
    private int TATInHrs;
    @Expose
    @SerializedName("Status")
    private String Status;
    @Expose
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @Expose
    @SerializedName("Closeresolution")
    private String Closeresolution;
    @Expose
    @SerializedName("TicketDescription")
    private String TicketDescription;
    @Expose
    @SerializedName("TicketId")
    private int TicketId;

    public ArrayList<CommentList> getTicketActivityLogDcs() {
        return TicketActivityLogDcs;
    }

    public int getTATInHrs() {
        return TATInHrs;
    }


    public String getStatus() {
        return Status;
    }


    public String getCreatedDate() {
        return CreatedDate;
    }


    public String getCloseresolution() {
        return Closeresolution;
    }


    public String getTicketDescription() {
        return TicketDescription;
    }


    public int getTicketId() {
        return TicketId;
    }


    public static class CommentList implements Serializable {
        public static final int HEADER = 0;
        public static final int VIEW = 1;

        @Expose
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @Expose
        @SerializedName("CreatedBy")
        private String CreatedBy;
        @Expose
        @SerializedName("Comment")
        private String Comment;
        private boolean isSectionHeader;
        private String  TicketDescription;

        public CommentList(@Nullable String comment, @Nullable String createdBy, @Nullable String createdDate, @Nullable String TicketDescription) {
            this.Comment = comment;
            this.CreatedBy = createdBy;
            this.CreatedDate = createdDate;
            this.TicketDescription = TicketDescription;

        }

        public String getTicketDescription() {
            return TicketDescription;
        }

        public void setTicketDescription(String ticketDescription) {
            TicketDescription = ticketDescription;
        }

        public void setToSectionHeader() {
            isSectionHeader = true;
        }

        public boolean isSectionHeader() {
            return isSectionHeader;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public String getComment() {
            return Comment;
        }
    }
}
