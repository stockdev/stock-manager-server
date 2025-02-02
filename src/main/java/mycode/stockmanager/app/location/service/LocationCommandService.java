package mycode.stockmanager.app.location.service;

import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;

public interface LocationCommandService  {

    LocationResponse createLocation(CreateLocationRequest createLocationRequest, long userId);

    LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id, long userId);

    LocationResponse deleteLocationByCode(String code);

    LocationResponse deleteLocationById(long id, long userId);

    void deleteAllLocationsAndResetSequence(long userId);
}
