package potboiler.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Pot implements Parcelable {

    private String namepot, avatar, name, age, rating, ratingCount, summ1, summ2, countdown, distance;

    public String getNamepot() {
        return namepot;
    }

    public void setNamepot(String namepot) {
        this.namepot = namepot;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getSumm1() {
        return summ1;
    }

    public void setSumm1(String summ1) {
        this.summ1 = summ1;
    }

    public String getSumm2() {
        return summ2;
    }

    public void setSumm2(String summ2) {
        this.summ2 = summ2;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Pot(){

    }

    public Pot(JSONObject jsonObject){
        try{
            namepot = jsonObject.getString("namepot");
            avatar = jsonObject.getString("avatar");
            name = jsonObject.getString("name");
            age = jsonObject.getString("age");
            rating = jsonObject.getString("rating");
            ratingCount = jsonObject.getString("rating_count");
            summ1 = jsonObject.getString("summ1");
            summ2 = jsonObject.getString("summ2");
            countdown = jsonObject.getString("countdown");
            distance = jsonObject.getString("distance");
        }
        catch (Exception e){

        }
    }

    protected Pot(Parcel in) {
        namepot = in.readString();
        avatar = in.readString();
        name = in.readString();
        age = in.readString();
        rating = in.readString();
        ratingCount = in.readString();
        summ1 = in.readString();
        summ2 = in.readString();
        countdown = in.readString();
        distance = in.readString();
    }

    public static final Creator<Pot> CREATOR = new Creator<Pot>() {
        @Override
        public Pot createFromParcel(Parcel in) {
            return new Pot(in);
        }

        @Override
        public Pot[] newArray(int size) {
            return new Pot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namepot);
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(rating);
        dest.writeString(ratingCount);
        dest.writeString(summ1);
        dest.writeString(summ2);
        dest.writeString(countdown);
        dest.writeString(distance);
    }
}
