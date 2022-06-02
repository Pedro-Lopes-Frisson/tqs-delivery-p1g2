package ua.tqs.delivera.models;

public class RiderDTO {
    private String name;
    private String email;
    private String password;
    private boolean available;
    private Location currentLocation;
    private int numberOfReviews;
    private int sumOfReviews;

    public RiderDTO(String email, String name, String password, boolean available, Location currentLocation, int numberOfReviews, int sumOfReviews){
        this.email = email;
        this.name = name;
        this.password = password;
        this.available = available;
        this.currentLocation=currentLocation;
        this.numberOfReviews=numberOfReviews;
        this.sumOfReviews = sumOfReviews;
    }

    public String getEmail(){
        return this.email;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean isAvailable(){
        return this.available;
    }

    public Location getlLocation(){
        return this.currentLocation;
    }

    public int getNumberOfReviews(){
        return this.numberOfReviews;
    }

    public int getSumOfReviews(){
        return this.sumOfReviews;
    }

}
