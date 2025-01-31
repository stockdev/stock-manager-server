package mycode.stockmanager.app.location.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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

    @GetMapping("/exportLocations")
    public ResponseEntity<?> exportLocations(HttpServletResponse response) {
        try {

            LocationResponseList locations = locationQueryService.getAllLocations();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=locations.xlsx");


            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Locations");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Code");

            int rowCount = 0;
            for (LocationResponse location : locations.list()) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(location.code());
            }

            workbook.write(response.getOutputStream());
            workbook.close();

            locationCommandService.deleteAllLocationsAndResetSequence();

            return ResponseEntity.ok("Exported " + locations.list().size() + " locations to Excel successfully.");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while exporting locations: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAllLocations")
    public ResponseEntity<?> deleteAllArticles(){
        locationCommandService.deleteAllLocationsAndResetSequence();

        return ResponseEntity.ok("Deleted all locations");
    }
}
