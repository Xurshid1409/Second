package second.education.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import second.education.model.request.IIBRequest;
import second.education.model.response.AcceptAndRejectAndCheckDiploma;
import second.education.model.response.GetStatAllCountAndToday;
import second.education.model.response.StatisDirectionResponse;
import second.education.model.response.StatisDirectionResponseByFutureInst;
import second.education.repository.ApplicationRepository;
import second.education.repository.DirectionRepository;
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
    private final DirectionRepository directionRepository;

    @GetMapping("count")
    public ResponseEntity<?> getCountAndTodayCount() {
        GetStatAllCountAndToday getStatAllCountAndToday = applicationRepository.getCountTodayAndAllCount().get();
        return ResponseEntity.ok(getStatAllCountAndToday);
    }

    @GetMapping("{futureInstId}")
    public ResponseEntity<?> getStatByFutureInst(@PathVariable int futureInstId) {
        List<StatisDirectionResponse> allStatis = statService.getAllStatis(futureInstId);
        return ResponseEntity.ok(allStatis);
    }

    @PostMapping("IIBCheck")
    public String checkIIB(@RequestBody IIBRequest request) {
        return statService.checkIIB(request);
    }


    ////STATISTIC ALL UNIVERSITY

    @GetMapping("/AcceptAndRejectAndDiploma")
    public List<AcceptAndRejectAndCheckDiploma> getAllStatisticByUniver() {
        return statService.statisticAllUniversity();
    }
}
