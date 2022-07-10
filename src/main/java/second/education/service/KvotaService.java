package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.Kvota;
import second.education.model.request.KvotaRequest;
import second.education.model.response.KvotaResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DirectionRepository;
import second.education.repository.KvotaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KvotaService {

    private final KvotaRepository kvotaRepository;
    private final DirectionRepository directionRepository;

    @Transactional
    public Result createKvota(List<KvotaRequest> request) {
       try {
           List<Kvota> kvotas = new ArrayList<>();
           request.forEach(e -> {
               Kvota kvota = new Kvota();
               kvota.setLanguage(e.getLanguage());
               kvota.setTalimShakli(e.getTalimShakli());
               kvota.setKvotaSoni(e.getSoni());
               Direction direction = directionRepository.findById(e.getDirectionId()).get();
               kvota.setDirection(direction);
               kvotas.add(kvota);
           });
           kvotaRepository.saveAll(kvotas);

       return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
       } catch (Exception ex) {
           return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
       }
    }

    @Transactional
    public Result updateKvota(List<KvotaRequest> request) {
        try {

            List<Kvota> kvotas = new ArrayList<>();
            request.forEach(e -> {
                Kvota kvota = kvotaRepository.findById(e.getId()).get();
                kvota.setLanguage(e.getLanguage());
                kvota.setTalimShakli(e.getTalimShakli());
                kvota.setKvotaSoni(e.getSoni());
//                Direction direction = directionRepository.findById(e.getDirectionId()).get();
//                kvota.setDirection(direction);
                kvota.setModifiedDate(LocalDateTime.now());
                kvotas.add(kvota);
            });
            kvotaRepository.saveAll(kvotas);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public KvotaResponse getKvotaBYId(Integer kvotaId) {
        try {
            Kvota kvota = kvotaRepository.findById(kvotaId).get();
            return new KvotaResponse(kvota);
        } catch (Exception ex) {
            return new KvotaResponse();
        }
    }

    @Transactional
    public List<KvotaResponse> getAllKvota() {
        return kvotaRepository.findAll().stream().map(KvotaResponse::new).toList();
    }
}
