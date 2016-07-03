package com.vlille.checker.xml.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.vlille.checker.model.Metadata;
import com.vlille.checker.model.SetStationsInfo;
import com.vlille.checker.model.Station;
import com.vlille.checker.xml.BaseStationHandler;

public class StationsListHandler extends BaseStationHandler<SetStationsInfo> {

	private Metadata metadata = new Metadata();
	private Set<Station> stations = new HashSet<Station>();

	@Override
	public SetStationsInfo getResult() {
		return new SetStationsInfo(metadata, new ArrayList<Station>(stations));
	}
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase(StationsListTags.MARKERS.tag())) {
            metadata.setLastUpdate(System.currentTimeMillis());
			//metadata.setLatitude1e6(PositionTransformer.to1e6(getValue(attributes, StationsListTags.CENTER_LATITUDE)));
			//metadata.setLongitude1e6(PositionTransformer.to1e6(getValue(attributes, StationsListTags.CENTER_LONGITUDE)));
		}
		if (localName.equalsIgnoreCase(StationsListTags.MARKER.tag())) {
			Station station = new Station();
			station.id = Long.valueOf(getValue(attributes, StationsListTags.ID));
			station.setName(getValue(attributes, StationsListTags.NAME));
			final String valueLatitude = getValue(attributes, StationsListTags.LATITUDE);
			//station.setLatitudeE6(PositionTransformer.to1e6(valueLatitude));
			station.setLatitude(Double.valueOf(valueLatitude));
			final String valueLongitude = getValue(attributes, StationsListTags.LONGITUDE);
			station.setLongitude(Double.valueOf(valueLongitude));
			//station.setLongitudeE6(PositionTransformer.to1e6(valueLongitude));

			stations.add(station);
		}
	}

	private String getValue(Attributes attributes, StationsListTags key) {
		return attributes.getValue(key.tag());
	}

}
