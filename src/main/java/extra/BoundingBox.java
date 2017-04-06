package extra;

import com.javadocmd.simplelatlng.LatLng;

/**
 * Created by lwesterhoff on 07/11/14.
 *
 * From https://github.com/veroborges/distProject3/blob/master/GeoServer/src/com/effectiveJava/GeoLocationService.java
 */
public class BoundingBox {

    private static final double EARTH_RADIUS_KM = 6371.009;

    private final LatLng minimumExtreme;
    private final LatLng maximumExtreme;

    public BoundingBox(LatLng point, final Double distance) {
        final LatLng[] extremePoints = getExtremePointsFrom(point, distance);
        this.minimumExtreme = extremePoints[0];
        this.maximumExtreme = extremePoints[1];
    }

    public BoundingBox(LatLng minimumExtreme, final LatLng maximumExtreme) {
        this.minimumExtreme = minimumExtreme;
        this.maximumExtreme = maximumExtreme;
    }

    public LatLng getMinimumExtreme() {
        return minimumExtreme;
    }

    public LatLng getMaximumExtreme() {
        return maximumExtreme;
    }

    public boolean containsPoint(final LatLng point) {
        return minimumExtreme.getLatitude() <= point.getLatitude() &&
                minimumExtreme.getLongitude() <= point.getLongitude() &&
                maximumExtreme.getLatitude() >= point.getLatitude() &&
                maximumExtreme.getLongitude() >= point.getLongitude();
    }

    public boolean intersect(final BoundingBox otherBox) {
        return minimumExtreme.getLatitude() <= otherBox.getMaximumExtreme().getLatitude() &&
                minimumExtreme.getLongitude() <= otherBox.getMaximumExtreme().getLongitude() &&
                maximumExtreme.getLatitude() >= otherBox.getMinimumExtreme().getLatitude() &&
                maximumExtreme.getLongitude() >= otherBox.getMinimumExtreme().getLongitude();
    }

    private static double deg2rad(final double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(final double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private static double getExtremeLatitudesDiffForPoint(final LatLng p1,
                                                          final double distance) {
        final double latitudeRadians = distance / EARTH_RADIUS_KM;
        final double diffLat = rad2deg(latitudeRadians);
        return diffLat;
    }

    private static double getExtremeLongitudesDiffForPoint(final LatLng p1,
                                                           final double distance) {
        double lat1 = p1.getLatitude();
        lat1 = deg2rad(lat1);
        final double longitudeRadius = Math.cos(lat1) * EARTH_RADIUS_KM;
        double diffLong = (distance / longitudeRadius);
        diffLong = rad2deg(diffLong);
        return diffLong;
    }

    private static LatLng[] getExtremePointsFrom(LatLng point, final Double distance) {
        final double longDiff = getExtremeLongitudesDiffForPoint(point, distance);
        final double latDiff = getExtremeLatitudesDiffForPoint(point, distance);
        LatLng p1 = new LatLng(point.getLatitude() - latDiff, point.getLongitude()
                - longDiff);
        p1 = validatePoint(p1);
        LatLng p2 = new LatLng(point.getLatitude() + latDiff, point.getLongitude()
                + longDiff);
        p2 = validatePoint(p2);

        return new LatLng[] { p1, p2 };
    }

    /**
     * Validates if the point passed has valid values in degrees i.e. latitude
     * lies between -90 and +90 and the longitude
     *
     * @param point
     * @return
     */
    private static LatLng validatePoint(LatLng point) {
        double lat = point.getLatitude();
        double lng = point.getLongitude();
        if (point.getLatitude() > 90) {
            lat = 90 - (lat - 90);
        }
        if (point.getLatitude() < -90) {
            lat = -90 - (lat + 90);
        }
        if (point.getLongitude() > 180) {
            lng = -180 + (lng - 180);
        }
        if (point.getLongitude() < -180) {
            lng = 180 + (lng + 180);
        }

        point = new LatLng(lat, lng);
        return point;
    }
}
