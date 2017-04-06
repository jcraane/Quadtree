import com.google.gson.Gson;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import extra.BoundingBox;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jamiecraane on 06/04/2017.
 */
public class StopTest {
    @Test
    public void findStopsWithinRadius() throws Exception {
        final InputStream is = getClass().getResourceAsStream("stops.json");
        final StopResponse stopResponse = new Gson().fromJson(new InputStreamReader(is), StopResponse.class);
        final List<StopResponse.Stop> stops = stopResponse.getPayload();

        final LatLng userLocation = new LatLng(52.103014, 5.183212);

        final int radiusToSearchInMeters = 250;

        QuadTree<StopResponse.Stop> quadTree = new QuadTree<>(-180.000000, -90.000000, 180.000000, 90.000000);
        stops.stream()
                .forEach(stop -> {
                    quadTree.set(stop.getLat(), stop.getLng(), stop);
                });

        final BoundingBox box = new BoundingBox(userLocation, (double) radiusToSearchInMeters / 1000);
        final LatLng maximumExtreme = box.getMaximumExtreme();
        final LatLng minimumExtreme = box.getMinimumExtreme();

        final List<Point<StopResponse.Stop>> result = quadTree.searchIntersect(userLocation, radiusToSearchInMeters);
        Assert.assertEquals(4, result.size());
        result.forEach(stop -> Assert.assertEquals("De Bilt, Tunneltje De Bilt".toLowerCase(), stop.getValue().getStopName().toLowerCase()));

        final List<Point<StopResponse.Stop>> result2 = quadTree.searchIntersect(minimumExtreme.getLatitude(), minimumExtreme.getLongitude(), maximumExtreme.getLatitude(), maximumExtreme.getLongitude());
        Assert.assertEquals(6, result2.size());
    }
}
