import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpatialTest {

    static List<Point> _pointList = null;

    private void LoadPointsFromFile(InputStream source) {
        String[] item;
        String[] lines = readAllTextFileLines(source);
        for (String line : lines) {
            item = line.split(",");
            _pointList.add(new Point(Double.parseDouble(item[2]), Double.parseDouble(item[1]), Double.parseDouble(item[0])));
        }
    }

    private static String[] readAllTextFileLines(InputStream source) {
        StringBuilder sb = new StringBuilder();

        try {
            String textLine;

            BufferedReader br = new BufferedReader(new InputStreamReader(source));

            while ((textLine = br.readLine()) != null) {
                sb.append(textLine);
                sb.append('\n');
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sb.length() == 0)
                sb.append("\n");
        }
        return sb.toString().split("\n");
    }

    @Test
    public void testTree(){
        _pointList = new ArrayList<Point>();
        final InputStream is = getClass().getResourceAsStream("points.txt");
//        URL classpathResource = Thread.currentThread().getContextClassLoader().getResource("");
//        String resourcePath = classpathResource.getPath()+"points.txt";
        LoadPointsFromFile(is);
        assertEquals("Expecting 844 points",844,_pointList.size());

        //http://spatialreference.org/ref/epsg/4326/
        QuadTree qt = new QuadTree(-180.000000, -90.000000, 180.000000, 90.000000);
        for(Point pt:_pointList)
        {
            qt.set(pt.getX(), pt.getY(), pt.getValue());
        }
        List<Point> points = qt.searchIntersect(-84.375,27.059,-78.75,31.952 );
        //System.out.print( Arrays.asList(points).toString());
        assertEquals(60,points.size());

    }


}
