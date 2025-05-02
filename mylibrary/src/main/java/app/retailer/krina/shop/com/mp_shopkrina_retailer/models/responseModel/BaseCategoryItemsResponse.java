package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseCategoryItemsResponse {
    @SerializedName("Basecats")
    ArrayList<BaseCatBean> mBaseCatBeanArrayList;
    @SerializedName("Categories")
    ArrayList<CategoryBean> mCategoryBeanArrayList;
    @SerializedName("SubCategories")
    ArrayList<SubCategoriesBean> mSubCategoriesBeanArrayList;

    public ArrayList<BaseCatBean> getmBaseCatBeanArrayList() {
        return mBaseCatBeanArrayList;
    }

    public void setmBaseCatBeanArrayList(ArrayList<BaseCatBean> mBaseCatBeanArrayList) {
        this.mBaseCatBeanArrayList = mBaseCatBeanArrayList;
    }

    public ArrayList<CategoryBean> getmCategoryBeanArrayList() {
        return mCategoryBeanArrayList;
    }

    public void setmCategoryBeanArrayList(ArrayList<CategoryBean> mCategoryBeanArrayList) {
        this.mCategoryBeanArrayList = mCategoryBeanArrayList;
    }

    public ArrayList<SubCategoriesBean> getmSubCategoriesBeanArrayList() {
        return mSubCategoriesBeanArrayList;
    }

    public void setmSubCategoriesBeanArrayList(ArrayList<SubCategoriesBean> mSubCategoriesBeanArrayList) {
        this.mSubCategoriesBeanArrayList = mSubCategoriesBeanArrayList;
    }




    public BaseCategoryItemsResponse(ArrayList<BaseCatBean> mBaseCatBeanArrayList, ArrayList<CategoryBean> mCategoryBeanArrayList, ArrayList<SubCategoriesBean> mSubCategoriesBeanArrayList/*, ArrayList<homecarebean> mhomeBeanArrayList*/) {
        this.mBaseCatBeanArrayList = mBaseCatBeanArrayList;
        this.mCategoryBeanArrayList = mCategoryBeanArrayList;
        this.mSubCategoriesBeanArrayList = mSubCategoriesBeanArrayList;

    }
public static class BaseCatBean{
    @SerializedName("BaseCategoryId")
    String BaseCategoryId;
    @SerializedName("Warehouseid")
    String Warehouseid;
    @SerializedName("BaseCategoryName")
    String BaseCategoryName;
    @SerializedName("LogoUrl")
    String LogoUrl;

    public BaseCatBean(String baseCategoryId, String warehouseid, String baseCategoryName, String logoUrl) {
        BaseCategoryId = baseCategoryId;
        Warehouseid = warehouseid;
        BaseCategoryName = baseCategoryName;
        LogoUrl = logoUrl;
    }

    public String getBaseCategoryId() {
        return BaseCategoryId;
    }

    public void setBaseCategoryId(String baseCategoryId) {
        BaseCategoryId = baseCategoryId;
    }

    public String getWarehouseid() {
        return Warehouseid;
    }

    public void setWarehouseid(String warehouseid) {
        Warehouseid = warehouseid;
    }

    public String getBaseCategoryName() {
        return BaseCategoryName;
    }

    public void setBaseCategoryName(String baseCategoryName) {
        BaseCategoryName = baseCategoryName;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }



    @Override
    public String toString() {
        return BaseCategoryId+""+BaseCategoryName+""+LogoUrl;
    }
}

    public class CategoryBean {
        @SerializedName("BaseCategoryId")
        String BaseCategoryId;
        @SerializedName("Categoryid")
        String Categoryid;
        @SerializedName("Warehouseid")
        String Warehouseid;
        @SerializedName("CategoryName")
        String CategoryName;
        @SerializedName("LogoUrl")
        String LogoUrl;

        public CategoryBean(String baseCategoryId, String categoryid, String warehouseid, String categoryName, String logoUrl) {
            BaseCategoryId = baseCategoryId;
            Categoryid = categoryid;
            Warehouseid = warehouseid;
            CategoryName = categoryName;
            LogoUrl = logoUrl;
        }

        public String getBaseCategoryId() {
            return BaseCategoryId;
        }

        public void setBaseCategoryId(String baseCategoryId) {
            BaseCategoryId = baseCategoryId;
        }

        public String getCategoryid() {
            return Categoryid;
        }

        public void setCategoryid(String categoryid) {
            Categoryid = categoryid;
        }

        public String getWarehouseid() {
            return Warehouseid;
        }

        public void setWarehouseid(String warehouseid) {
            Warehouseid = warehouseid;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getLogoUrl() {
            return LogoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            LogoUrl = logoUrl;
        }
        @Override
        public String toString() {
            return LogoUrl;
        }
    }

    public class SubCategoriesBean {
        @SerializedName("SubCategoryId")
        String SubCategoryId;
        @SerializedName("Categoryid")
        String Categoryid;
        @SerializedName("SubcategoryName")
        String SubcategoryName;

        public SubCategoriesBean(String subCategoryId, String categoryid, String subcategoryName) {
            SubCategoryId = subCategoryId;
            Categoryid = categoryid;
            SubcategoryName = subcategoryName;
        }

        public String getSubCategoryId() {
            return SubCategoryId;
        }

        public void setSubCategoryId(String subCategoryId) {
            SubCategoryId = subCategoryId;
        }

        public String getCategoryid() {
            return Categoryid;
        }

        public void setCategoryid(String categoryid) {
            Categoryid = categoryid;
        }

        public String getSubcategoryName() {
            return SubcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            SubcategoryName = subcategoryName;
        }

        @Override
        public String toString() {
            return SubcategoryName+""+SubCategoryId+""+Categoryid;
        }
    }



}
