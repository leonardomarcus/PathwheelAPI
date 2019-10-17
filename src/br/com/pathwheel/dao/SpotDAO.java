package br.com.pathwheel.dao;

import java.util.List;

import br.com.pathwheel.mapping.GeographicCoordinate;
import br.com.pathwheel.model.Spot;

public interface SpotDAO {
	Spot register(Spot spot);
	Spot fetch(long id);
	List<Spot> fetchByArea(List<GeographicCoordinate> verticesPolygon, int travelModeId);
	void report(long spotId, long spotUser, int sporReportTypeId);
	void remove(long spotId);
	boolean hasReport(long spotId, long spotUser, int sporReportTypeId);
	int countReports(long spotId, int sporReportTypeId);
	boolean byUser(long spotId, long userId);
}
