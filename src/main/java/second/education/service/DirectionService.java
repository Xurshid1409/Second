package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.FutureInstitution;
import second.education.model.request.DirectionRequest;
import second.education.model.response.DirectionResponse;
import second.education.model.response.ResponseMessage;
import second.education.model.response.Result;
import second.education.repository.DirectionRepository;
import second.education.repository.FutureInstitutionRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectionService {

    private final DirectionRepository directionRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;

    @Transactional
    public Result createDirection(DirectionRequest request) {

        try {
            Direction direction = new Direction();
            direction.setName(request.getName());
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstitutionId()).get();
            direction.setFutureInstitution(futureInstitution);
            directionRepository.save(direction);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true, direction);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateDirection(int directionId, DirectionRequest request) {

        try {
            Direction direction = directionRepository.findById(directionId).get();
            direction.setName(request.getName());
//            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstitutionId()).get();
//            direction.setFutureInstitution(futureInstitution);
            directionRepository.save(direction);
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true, direction);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    public List<DirectionResponse> getAllDirection(Integer futureInstitutionId) {
        return directionRepository.findAllByFutureInstitutionId(futureInstitutionId)
                .stream().map(DirectionResponse::new).toList();
    }

    public DirectionResponse getDirectionById(int directionId) {
        try {
            Direction direction = directionRepository.findById(directionId).get();
            return new DirectionResponse(direction);
        } catch (Exception ex) {
            return new DirectionResponse();
        }
    }

    public Result deleteDirection(int directionId) {
        try {
            directionRepository.deleteById(directionId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

}
