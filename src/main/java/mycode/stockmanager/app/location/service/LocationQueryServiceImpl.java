package mycode.stockmanager.app.location.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.LocationResponseList;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class LocationQueryServiceImpl implements LocationQueryService{

    private LocationRepository locationRepository;

    @Override
    public LocationResponse getLocationById(long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse getLocationByCode(String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new NoLocationFound("No location with this code found"));

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponseList getAllLocations() {
        List<Location> list = locationRepository.findAll();

        List<LocationResponse> responses = new ArrayList<>();

        list.forEach(location -> {

            responses.add(LocationMapper.locationToResponseDto(location));
        });

        return new LocationResponseList(responses);
    }
}
