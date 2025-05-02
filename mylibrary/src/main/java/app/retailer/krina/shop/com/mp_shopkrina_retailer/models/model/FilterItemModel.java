package app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model;

public class FilterItemModel implements Comparable<FilterItemModel> {

    private String name;
    private String category;
    private boolean isSectionHeader;
    public boolean isChecked = false;
    public FilterItemModel(String name, String category)
    {
        this.name = name;
        this.category = category;
        isSectionHeader = false;
    }
    public FilterItemModel(String name, String category, Boolean header)
    {
        this.name = name;
        this.category = category;
        isSectionHeader = header;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setToSectionHeader()
    {
        isSectionHeader = true;
    }

    public boolean isSectionHeader()
    {
        return isSectionHeader;
    }

    @Override
    public int compareTo(FilterItemModel other) {
        return this.category.compareTo(other.category);
    }

    @Override
    public String toString() {
        return category;
    }
}
