/**
 * Created by Vsfmqueen on 10.12.2014.
 */
public class Distance {
    private String distance;
    private String duration;

    public Distance() {
    }

    public Distance(String distance, String duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance1 = (Distance) o;

        if (!distance.equals(distance1.distance)) return false;
        if (!duration.equals(distance1.duration)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = distance.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
