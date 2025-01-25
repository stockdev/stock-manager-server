package mycode.stockmanager.app.location.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import mycode.stockmanager.app.location.exceptions.LocationAlreadyExists;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class LocationCommandServiceImpl implements LocationCommandService{

    private LocationRepository locationRepository;

    @Override
    public LocationResponse createLocation(CreateLocationRequest createLocationRequest) {
        Location location = LocationMapper.createLocationRequestToLocation(createLocationRequest);
        List<Location> list = locationRepository.findAll();

        list.forEach(location1 -> {
            if(location.getCode().equals(location1.getCode())){
                throw new LocationAlreadyExists("Location with this code already exists");
            }
        });

        locationRepository.saveAndFlush(location);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        location.setCode(updateLocationRequest.code());

        locationRepository.save(location);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse deleteLocationByCode(String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new NoLocationFound("No location with this code found"));

        LocationResponse locationResponse = LocationMapper.locationToResponseDto(location);

        locationRepository.delete(location);

        return locationResponse;
    }

    @Transactional
    public void deleteAllLocationsAndResetSequence() {
        locationRepository.deleteAll();
        locationRepository.resetLocationSequence();
    }

    @Override
    public LocationResponse deleteLocationById(long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        LocationResponse locationResponse = LocationMapper.locationToResponseDto(location);

        locationRepository.delete(location);

        return locationResponse;
    }
}
