package cotato.networking.weather_api.common.station;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;

@Service
public class CoordService {

	private final CoordinateTransform transform;

	public CoordService() {
		CRSFactory crsFactory = new CRSFactory();
		CoordinateTransformFactory coordinateTransformFactory = new CoordinateTransformFactory();

		var wgs84 = crsFactory.createFromName("EPSG:4326");
		var koreaTm = crsFactory.createFromName("EPSG:5174");   // ← 핵심 수정

		transform = coordinateTransformFactory.createTransform(wgs84, koreaTm);
	}

	public TmCoord toTm(double lat, double lon) {
		var src = new ProjCoordinate(lon, lat);
		var dst = new ProjCoordinate();

		transform.transform(src, dst);

		return TmCoord.builder()
			.x(dst.x)
			.y(dst.y)
			.build();
	}
}
