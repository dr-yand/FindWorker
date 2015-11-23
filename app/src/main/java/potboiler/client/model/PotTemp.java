package potboiler.client.model;

import java.sql.Time;
import java.util.Date;

public class PotTemp {

    private Category category;
    private Category subcategory;

    private String name, description;

    private Date date;
    private Date time;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.subcategory = null;
    }

    public Category getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Category subcategory) {
        this.subcategory = subcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
