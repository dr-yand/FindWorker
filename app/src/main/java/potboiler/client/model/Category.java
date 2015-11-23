package potboiler.client.model;

import org.json.JSONObject;

public class Category {

    private String id, name, text;

    public Category(){

    }

    public Category(JSONObject jsonObject){
        try{
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            text = jsonObject.getString("text");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
