package pk.com.pieinthesky.app.beans;

import java.io.Serializable;

public class Cuisine implements Serializable {
    private String CuisineId;
    private String CuisineName;

    public String getCuisineId() {
        return CuisineId;
    }

    public String getCuisineName() {
        return CuisineName;
    }

    public void setCuisineId( String CuisineId ) {
        this.CuisineId = CuisineId;
    }

    public void setCuisineName( String CuisineName ) {
        this.CuisineName = CuisineName;
    }


    @Override
    public String toString() {
        return CuisineName;
    }
}
