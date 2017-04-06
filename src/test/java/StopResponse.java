import java.util.List;

/**
 * Created by jamiecraane on 06/04/2017.
 */
public class StopResponse {
    private List<Stop> payload;

    public List<Stop> getPayload() {
        return payload;
    }

    public static class Stop {
        private double lat ,lng;
        private String stopCode;
        private String stopName;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getStopCode() {
            return stopCode;
        }

        public String getStopName() {
            return stopName;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Stop{");
            sb.append(", code='").append(stopCode).append('\'');
            sb.append(", stopName='").append(stopName).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
