package br.com.pathwheel.dao;

import java.util.List;

import br.com.pathwheel.mapping.GeographicCoordinate;
import br.com.pathwheel.model.PavementSample;

public interface PavementSampleDAO {
	void register(PavementSample data, String smartDevice);	
	List<PavementSample> fetchByArea(List<GeographicCoordinate> verticesPolygon, int travelModeId);
}
