package mycode.stockmanager.app.stock.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.stockmanager.app.stock.dtos.CreateStockRequest;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.StockResponseList;
import mycode.stockmanager.app.stock.dtos.UpdateStockRequest;
import mycode.stockmanager.app.stock.service.StockCommandService;
import mycode.stockmanager.app.stock.service.StockQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/stock-manager/api/stock")
@CrossOrigin
@Slf4j
public class StockController {

    private StockCommandService stockCommandService;
    private StockQueryService stockQueryService;

    @GetMapping("/getAllStocks")
    public ResponseEntity<StockResponseList> getAllStocks(){
        return new ResponseEntity<>(stockQueryService.getAllStocks(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createStockTransaction")
    public ResponseEntity<StockResponse> createStock(@RequestBody CreateStockRequest createStockRequest){
        return new ResponseEntity<>(stockCommandService.createStockTransaction(createStockRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStockTransaction/{stockId}")
    public ResponseEntity<StockResponse> updateStock(@RequestBody UpdateStockRequest updateStockRequest, @PathVariable Long stockId){
        return new ResponseEntity<>(stockCommandService.updateStockTransaction(stockId,updateStockRequest), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteStockTransaction/{stockID}")
    public ResponseEntity<StockResponse> deleteStock(@PathVariable Long stockID){
        return new ResponseEntity<>(stockCommandService.deleteStockTransaction(stockID), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllStocks")
    public ResponseEntity<String> deleteAllStocks(){
        stockCommandService.deleteAllStocksAndResetSequence();
        return new ResponseEntity<>("All stocks deleted", HttpStatus.OK);
    }
}
