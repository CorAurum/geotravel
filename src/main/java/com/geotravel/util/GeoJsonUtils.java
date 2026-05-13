package com.geotravel.util;

import org.locationtech.jts.geom.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

public class GeoJsonUtils {

    private static final GeoJSONWriter writer = new GeoJSONWriter();
    private static final GeoJSONReader reader = new GeoJSONReader();

    // Geometry JTS → String GeoJSON (para enviar al frontend)
    public static String toGeoJson(Geometry geometry) {
        if (geometry == null) return null;
        return writer.write(geometry).toString();
    }

    // String GeoJSON → Geometry JTS (cuando el frontend te manda una geometría)
    public static Geometry fromGeoJson(String geoJson) {
        if (geoJson == null || geoJson.isBlank()) return null;
        try {
            return reader.read(geoJson);
        } catch (Exception e) {
            throw new IllegalArgumentException("GeoJSON inválido: " + e.getMessage());
        }
    }
}