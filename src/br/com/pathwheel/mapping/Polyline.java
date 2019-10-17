package br.com.pathwheel.mapping;

import java.util.ArrayList;
import java.util.List;

public class Polyline {	

	public static List<GeographicCoordinate> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<GeographicCoordinate> path = new ArrayList<GeographicCoordinate>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            GeographicCoordinate coordenada = new GeographicCoordinate();
            coordenada.setLatitude(lat * 1e-5);
            coordenada.setLongitude(lng * 1e-5);
            path.add(coordenada);
        }

        return path;
    }
	
	public static String encode(final List<GeographicCoordinate> coordenadas) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final GeographicCoordinate point : coordenadas) {
            long lat = Math.round(point.getLatitude() * 1e5);
            long lng = Math.round(point.getLongitude() * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encodeResult(dLat, result);
            encodeResult(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }	
	
	private static void encodeResult(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
        result.append(Character.toChars((int) (v + 63)));
    }
	
	public static void main(String[] args) {
		List<GeographicCoordinate> coordinates = new ArrayList<>();
		coordinates.add(new GeographicCoordinate(-13.006010000000002d,-38.46041d));
		coordinates.add(new GeographicCoordinate(-12.994890000000002,-38.443220000000004));
		System.out.println(Polyline.encode(coordinates));
		String polyline = "jfknAtxviFxAdCdCcBvAu@HG_B_BsAeBGIkAgCISa@iDFiB@_@EYYe@GOYcASyA]mCCQ[qAW}@q@gAs@w@YYi@a@{EaEmAcAk@a@o@i@_B_BeAoAw@cAaAsAEGgAiBeEeGcAyAGKiAqBy@yA_@}@cA{CUq@QkAS_BYq@GKCAECG?E@GBCDADENL`@LfAAt@CTIn@_C^eC^@uAIo@Oq@k@}ASe@Ug@aAcBu@WSE";
		coordinates = Polyline.decode(polyline);
		System.out.println("new size: "+coordinates.size());
	}
}
