package mycode.stockmanager.app.location.service;

import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.LocationResponseList;

public interface LocationQueryService {
    LocationResponse getLocationById(long id);

    LocationResponse getLocationByCode(String code);

    LocationResponseList getAllLocations();
}
