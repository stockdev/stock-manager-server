package mycode.stockmanager.app.stock.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.stockmanager.app.stock.dtos.CreateStockRequest;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.UpdateStockRequest;
import mycode.stockmanager.app.stock.service.StockCommandService;
import mycode.stockmanager.app.stock.service.StockQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/stock-manager/api/stock")
@CrossOrigin
@Slf4j
public class StockController {

    private StockCommandService stockCommandService;
    private StockQueryService stockQueryService;

    @GetMapping("/getStockById/{stockId}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long stockId){
        return new ResponseEntity<>(stockQueryService.getStockById(stockId), HttpStatus.OK);
    }

    @GetMapping("/getStockByOrderNumber/{orderNumber}")
    public ResponseEntity<StockResponse> getStockByOrderNumber(@PathVariable int orderNumber){
        return new ResponseEntity<>(stockQueryService.getStockByOrderNumber(orderNumber), HttpStatus.OK);
    }

    @PostMapping("/createStockTransaction")
    public ResponseEntity<StockResponse> createStock(@RequestBody CreateStockRequest createStockRequest){
        return new ResponseEntity<>(stockCommandService.createStockTransaction(createStockRequest), HttpStatus.CREATED);
    }

    @PutMapping("/updateStockTransaction/{stockId}")
    public ResponseEntity<StockResponse> updateStock(@RequestBody UpdateStockRequest updateStockRequest, @PathVariable Long stockId){
        return new ResponseEntity<>(stockCommandService.updateStockTransaction(stockId,updateStockRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deleteStockTransaction/{stockID}")
    public ResponseEntity<StockResponse> deleteStock(@PathVariable Long stockID){
        return new ResponseEntity<>(stockCommandService.deleteStockTransaction(stockID), HttpStatus.OK);
    }
}
