package ua.tqs.delivera.datamodels;

import ua.tqs.delivera.models.Location;

public class RiderDTO {
    private String namedto;
    private String emaildto;
    private String passworddto;
    private boolean availabledto;
    private Location currentLocationdto;
    private int numberOfReviewsdto;
    private int sumOfReviewsdto;

    public RiderDTO(String email, String name, String password, boolean available, Location currentLocation, int numberOfReviews, int sumOfReviews){
        this.emaildto =email;
        this.namedto = name;
        this.passworddto = password;
        this.availabledto=available;
        this.currentLocationdto=currentLocation;
        this.numberOfReviewsdto=numberOfReviews;
        this.sumOfReviewsdto = sumOfReviews;
    }

    public String getEmaildto(){
        return this.emaildto;
    }

    public String getNamedto(){
        return this.namedto;
    }

    public String getPassworddto(){
        return this.passworddto;
    }

    public boolean isAvailabledto(){
        return this.availabledto;
    }

    public Location getLocationdto(){
        return this.currentLocationdto;
    }

    public int getNumberOfReviewsdto(){
        return this.numberOfReviewsdto;
    }

    public int getSumOfReviewsdto(){
        return this.sumOfReviewsdto;
    }

    public void setPassworddto(String pw){
        this.passworddto = pw;
    }

    public void setEmail(String email) {
        this.emaildto = email;
    }

}
