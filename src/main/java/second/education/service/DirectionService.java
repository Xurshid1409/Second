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
import java.util.ArrayList;
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
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true, new DirectionResponse(direction));
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
            return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true,new DirectionResponse(direction));
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public List<DirectionResponse> getAllDirectionByFutureInst(Integer futureInstitutionId) {
        List<Direction> directions = directionRepository.findAllByFutureInstitutionId(futureInstitutionId);
        List<DirectionResponse> directionResponses = new ArrayList<>();
        directions.forEach(d -> {
            DirectionResponse directionResponse = new DirectionResponse();
            directionResponse.setId(d.getId());
            directionResponse.setName(d.getName());
            directionResponse.setFutureInstitutionId(d.getFutureInstitution().getId());
            directionResponse.setFutureInstitutionName(d.getFutureInstitution().getName());
            directionResponses.add(directionResponse);
        });
        return directionResponses;
    }

    @Transactional(readOnly = true)
    public List<DirectionResponse> getAllDirection() {
        List<Direction> directions = directionRepository.findAll();
        List<DirectionResponse> directionResponses = new ArrayList<>();
        directions.forEach(d -> {
            DirectionResponse directionResponse = new DirectionResponse();
            directionResponse.setId(d.getId());
            directionResponse.setName(d.getName());
            directionResponse.setFutureInstitutionId(d.getFutureInstitution().getId());
            directionResponse.setFutureInstitutionName(d.getFutureInstitution().getName());
            directionResponses.add(directionResponse);
        });
        return directionResponses;
    }

    @Transactional(readOnly = true)
    public DirectionResponse getDirectionById(int directionId) {
        try {
            Direction direction = directionRepository.findById(directionId).get();
            DirectionResponse directionResponse = new DirectionResponse();
            directionResponse.setId(direction.getId());
            directionResponse.setName(direction.getName());
            directionResponse.setFutureInstitutionId(direction.getFutureInstitution().getId());
            directionResponse.setFutureInstitutionName(direction.getFutureInstitution().getName());
            return directionResponse;
        } catch (Exception ex) {
            return new DirectionResponse();
        }
    }

    @Transactional
    public Result deleteDirection(int directionId) {
        try {
            directionRepository.deleteById(directionId);
            return new Result(ResponseMessage.SUCCESSFULLY_DELETED.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_DELETED.getMessage(), false);
        }
    }

}
