package models;

import java.util.Objects;

import models.enums.RequestType;

public class Request {
    private int floor; // The floor number for which the request is made either for pickup or destination
    RequestType requestType; // The type of request, either pickup(from hall call) or destination -- PICKUP_UP, PICKUP_DOWN, DESTINATION(from inside the elevator)
    
    public Request(int floor, RequestType requestType) {
        this.floor = floor;
        this.requestType = requestType;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return floor == request.floor && requestType == request.requestType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, requestType);
    }

    

    
    
}
