import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsfmqueen on 10.12.2014.
 */

public class Location {
    private String address;
    private List<Double> locations;

    public Location() {
    }

    public Location(String address, List<Double> locations) {
        this.address = address;
        this.locations = locations;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Double> getLocations() {
        return locations;
    }

    public void setLocations(List<Double> locations) {
        this.locations = locations;
    }

    public String getStringLocations(){
        return new StringBuilder().append(this.locations.get(1)).append(",").append(this.locations.get(0)).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!address.equals(location.address)) return false;
        if (!locations.equals(location.locations)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + locations.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", locations=" + locations +
                '}';
    }
}
