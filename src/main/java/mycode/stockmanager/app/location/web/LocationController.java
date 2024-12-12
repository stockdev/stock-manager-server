package mycode.stockmanager.app.location.web;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponseList;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import mycode.stockmanager.app.location.service.LocationCommandService;
import mycode.stockmanager.app.location.service.LocationQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-manager/api/location")
@AllArgsConstructor
public class LocationController {

    private final LocationCommandService locationCommandService;
    private final LocationQueryService locationQueryService;

    @GetMapping("/getLocationById/{locationId}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable long locationId) {
        return new ResponseEntity<>(locationQueryService.getLocationById(locationId), HttpStatus.OK);
    }

    @GetMapping("/getLocationByCode/{code}")
    public ResponseEntity<LocationResponse> getLocationByCode(@PathVariable String code) {
        return new ResponseEntity<>(locationQueryService.getLocationByCode(code), HttpStatus.OK);
    }

    @GetMapping("/getAllLocations")
    public ResponseEntity<LocationResponseList> getAllLocations(){
        return new ResponseEntity<>(locationQueryService.getAllLocations(), HttpStatus.OK);
    }

    @PostMapping("/createLocation")
    public ResponseEntity<LocationResponse> createLocation(@RequestBody CreateLocationRequest createLocationRequest) {
        return new ResponseEntity<>(locationCommandService.createLocation(createLocationRequest), HttpStatus.CREATED);
    }

    @PutMapping("/updateLocation/{locationId}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable long locationId, @RequestBody UpdateLocationRequest updateLocationRequest) {
        return new ResponseEntity<>(locationCommandService.updateLocation(updateLocationRequest, locationId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteLocationById/{locationId}")
    public ResponseEntity<LocationResponse> deleteLocationById(@PathVariable long locationId) {
        return new ResponseEntity<>(locationCommandService.deleteLocationById(locationId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteLocationByCode/{locationCode}")
    public ResponseEntity<LocationResponse> deleteLocationByCode(@PathVariable String locationCode) {
        return new ResponseEntity<>(locationCommandService.deleteLocationByCode(locationCode), HttpStatus.OK);
    }
}
