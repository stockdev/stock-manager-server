package mycode.stockmanager.app.location.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.LocationResponseList;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public LocationResponseList getAllLocations(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Location> locationPage;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            locationPage = locationRepository.findAll(pageable);
        } else {
            locationPage= locationRepository.findByCodeContainingIgnoreCase(searchTerm, pageable);
        }

        if (locationPage.isEmpty()) {
            throw new NoLocationFound("No locations found");
        }

        List<LocationResponse> responses = locationPage.getContent()
                .stream()
                .map(LocationMapper::locationToResponseDto)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return new LocationResponseList(responses, locationPage.getNumber(), locationPage.getTotalPages(), locationPage.getTotalElements());
    }
}
