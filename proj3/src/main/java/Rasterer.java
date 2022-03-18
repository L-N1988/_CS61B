import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double UL_LON = MapServer.ROOT_ULLON;
    private static final double LR_LON = MapServer.ROOT_LRLON;
    private static final double LR_LAT = MapServer.ROOT_LRLAT;
    private static final double UL_LAT = MapServer.ROOT_ULLAT;
    private static final double MAXLONDPP = (LR_LON - UL_LON) / MapServer.TILE_SIZE;
    private double ullon;
    private double lrlon;
    private double ullat;
    private double lrlat;
    private double w;
    private double h;
    private double raster_UL_LON;
    private double raster_LR_LON;
    private double raster_LR_LAT;
    private double raster_UL_LAT;
    private boolean querySuccess;
    private String[][] renderGrid;

    public Rasterer() {
        querySuccess = true;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "renderGrid"   : String[][], the files to display. <br>
     * "raster_UL_LON" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_UL_LAT" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_LR_LON" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_LR_LAT" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "querySuccess" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        readIn(params);
        if (ullon < UL_LON || lrlon > LR_LON || ullat > UL_LAT || lrlat < LR_LAT) {
            querySuccess = false;
        }
        // dpp calculate
        double lonDpp = (lrlon - ullon) / w;
        // calculate depth
        int depth = 0;
        double lonUnit;
        double latUnit;
        while (MAXLONDPP / Math.pow(2, depth) > lonDpp && depth < 8) {
            depth += 1;
        }
        if (depth == 8) {
            depth = 7;
        }
        lonUnit = (LR_LON - UL_LON) / Math.pow(2, depth);
        latUnit = (UL_LAT - LR_LAT) / Math.pow(2, depth);
        double x1, x2;
        double y1, y2;
        x1 = (ullon - UL_LON) / lonUnit;
        x2 = (lrlon - UL_LON) / lonUnit;
        y1 = (UL_LAT - ullat) / latUnit;
        y2 = (UL_LAT - lrlat) / latUnit;
        raster_UL_LON = UL_LON + ((int) x1) * lonUnit;
        raster_LR_LON = UL_LON + ((int) x2 + 1) * lonUnit;
        raster_UL_LAT = UL_LAT - ((int) y1) * latUnit;
        raster_LR_LAT = UL_LAT - ((int) y2 + 1) * latUnit;
        // calculate tiles number
        int m = (int) x2 - (int) x1 + 1;
        int n = (int) y2 - (int) y1 + 1;
        renderGrid = new String[n][m];
        for (int j = (int) y1; j <= (int) y2; j++) {
            for (int i = (int) x1; i <= (int) x2; i++) {
                n = i - (int) x1;
                m = j - (int) y1;
                renderGrid[m][n] = "d" + depth + "_x" + i + "_y" + j + ".png";
            }
        }
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", raster_UL_LON);
        results.put("raster_ul_lat", raster_UL_LAT);
        results.put("raster_lr_lon", raster_LR_LON);
        results.put("raster_lr_lat", raster_LR_LAT);
        results.put("depth", depth);
        results.put("query_success", querySuccess);
        return results;
    }

    private void readIn(Map<String, Double> params) {
        ullon = params.get("ullon");
        lrlon = params.get("lrlon");
        w = params.get("w");
        h = params.get("h");
        ullat = params.get("ullat");
        lrlat = params.get("lrlat");
    }

}
