package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.FutureInstitutionRequest;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.FutureInstitutionRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureInstitutionService {

    private final FutureInstitutionRepository futureInstitutionRepository;

    public Result createFutureInstitution(FutureInstitutionRequest request) {

        try {
            FutureInstitution futureInstitution = new FutureInstitution();
            futureInstitution.setName(request.getName());
            futureInstitutionRepository.save(futureInstitution);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    public Result updateFutureInstitution(int id, FutureInstitutionRequest request) {

        try {
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(id).get();
            futureInstitution.setName(request.getName());
            futureInstitution.setModifiedDate(LocalDateTime.now());
            futureInstitutionRepository.save(futureInstitution);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

}
