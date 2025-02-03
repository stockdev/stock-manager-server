package mycode.stockmanager.app.location.service;

import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;

public interface LocationCommandService  {

    LocationResponse createLocation(CreateLocationRequest createLocationRequest);

    LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id);

    String deleteLocationByCode(String code);

    void deleteAllLocationsAndResetSequence();
}
