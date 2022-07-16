package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.api_model.iib_api.Data;
import second.education.model.request.IIBRequest;
import second.education.model.response.GetStatAllCountAndToday;
import second.education.model.response.GetStatisByDirection;
import second.education.repository.ApplicationRepository;
import second.education.service.StatService;
import second.education.service.api.IIBServiceApi;

import java.util.List;

@RestController
@RequestMapping("api/statistic")
@RequiredArgsConstructor
public class StatController {

    private final ApplicationRepository applicationRepository;
    private final StatService statService;
    private final IIBServiceApi iibServiceApi;

    @GetMapping("count")
    public ResponseEntity<?> getCountAndTodayCount() {
        GetStatAllCountAndToday getStatAllCountAndToday = applicationRepository.getCountTodayAndAllCount().get();
        return ResponseEntity.ok(getStatAllCountAndToday);
    }

    @GetMapping("/{futureInstId}")
    public ResponseEntity<?> getStatByFutureInst(@PathVariable int futureInstId) {
        List<GetStatisByDirection> statisByDirections = statService.getStatisByDirections(futureInstId);
        return ResponseEntity.ok(statisByDirections);
    }

    @PostMapping("IIBCheck")
    public String checkIIB(@RequestBody IIBRequest request) {
       return statService.checkIIB(request);
    }
}
