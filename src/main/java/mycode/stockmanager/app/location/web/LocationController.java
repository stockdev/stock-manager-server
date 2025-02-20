package mycode.stockmanager.app.location.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponseList;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.service.LocationCommandService;
import mycode.stockmanager.app.location.service.LocationQueryService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/stock-manager/api/location")
@AllArgsConstructor
public class LocationController {

    private final LocationCommandService locationCommandService;
    private final LocationQueryService locationQueryService;


    @GetMapping("/getAllLocations")
    public ResponseEntity<LocationResponseList> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String searchTerm
    ) {
        LocationResponseList response = locationQueryService.getAllLocations(page, size, searchTerm);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createLocation")
    public ResponseEntity<LocationResponse> createLocation(@RequestBody CreateLocationRequest createLocationRequest) {
        return new ResponseEntity<>(locationCommandService.createLocation(createLocationRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateLocation/{code}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable String code, @RequestBody UpdateLocationRequest updateLocationRequest
    ) {
        return new ResponseEntity<>(locationCommandService.updateLocation(updateLocationRequest, code), HttpStatus.OK);
    }


    @DeleteMapping("/deleteLocationByCode/{locationCode}")
    public ResponseEntity<LocationResponse> deleteLocationByCode(@PathVariable String locationCode) {
        return new ResponseEntity<>(locationCommandService.deleteLocationByCode(locationCode), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllLocations")
    public ResponseEntity<?> deleteAllLocations() {
        locationCommandService.deleteAllLocationsAndResetSequence();
        return ResponseEntity.ok("Deleted all locations");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/importExcel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            ImportResponse importResponse = locationCommandService.importLocationsFromExcel(file);
            return ResponseEntity.ok(importResponse);
        } catch (Exception e) {
            List<String> errorList = List.of("Error importing: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ImportResponse(0, errorList));
        }
    }



}