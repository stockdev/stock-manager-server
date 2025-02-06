package mycode.stockmanager.app.location.service;

import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import org.springframework.web.multipart.MultipartFile;

public interface LocationCommandService  {

    LocationResponse createLocation(CreateLocationRequest createLocationRequest);

    LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id);

    String deleteLocationByCode(String code);

    void deleteAllLocationsAndResetSequence();
}
