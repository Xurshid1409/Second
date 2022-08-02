package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import second.education.domain.AdminEntity;
import second.education.domain.User;
import second.education.domain.classificator.FutureInstitution;
import second.education.domain.classificator.Role;
import second.education.domain.classificator.University;
import second.education.model.request.DefaultRole;
import second.education.model.request.UserRequest;
import second.education.model.response.*;
import second.education.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FutureInstitutionRepository futureInstitutionRepository;
    private final AdminEntityRepository adminEntityRepository;
    private final UniversityRepository universityRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public Result createInstitutionAdmin(UserRequest request) {

        try {
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isEmpty()) {
                User user = new User();
                user.setPhoneNumber(request.getPinfl());
                user.setPassword(passwordEncoder.encode(request.getPinfl()));
                Role role = roleRepository.findByName(DefaultRole.ROLE_UADMIN.getMessage()).get();
                user.setRole(role);
                User saveUser = userRepository.save(user);
                AdminEntity adminEntity = new AdminEntity();
                if (request.getFutureInstId() == null) {
                    adminEntity.setFutureInstitution(null);
                } else {
                    FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                    adminEntity.setFutureInstitution(futureInstitution);
                }
                List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                adminEntity.setUniversities(universities);
                adminEntity.setUser(saveUser);
                adminEntityRepository.save(adminEntity);
                return new Result(ResponseMessage.SUCCESSFULLY_SAVED.getMessage(), true);
            }
            return new Result(ResponseMessage.ALREADY_EXISTS.getMessage(), false);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_SAVED.getMessage(), false);
        }
    }

    @Transactional
    public Result updateInstitutionAdmin(Integer adminEntityId, UserRequest request) {

        try {
            AdminEntity adminEntity = adminEntityRepository.findById(adminEntityId).get();
            Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(request.getPinfl());
            if (byPhoneNumber.isPresent()) {
                if (adminEntity.getUser().getId().equals(byPhoneNumber.get().getId())) {
                    adminEntity.getUser().setPhoneNumber(request.getPinfl());
                    adminEntity.getUser().setPassword(passwordEncoder.encode(request.getPinfl()));
                    User save = userRepository.save(byPhoneNumber.get());
                    if (request.getFutureInstId() == null) {
                        adminEntity.setFutureInstitution(null);
                    } else {
                        FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
                        adminEntity.setFutureInstitution(futureInstitution);
                    }
                    adminEntity.getUniversities().clear();
                    List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
                    adminEntity.setUniversities(universities);
                    adminEntity.setUser(save);
                    adminEntityRepository.save(adminEntity);
                    return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
                }
                return new Result("bu pinfl oldin qo'shilgan", false);
            }
            adminEntity.getUser().setPhoneNumber(request.getPinfl());
            adminEntity.getUser().setPassword(passwordEncoder.encode(request.getPinfl()));
            User save = userRepository.save(adminEntity.getUser());
            FutureInstitution futureInstitution = futureInstitutionRepository.findById(request.getFutureInstId()).get();
            adminEntity.setFutureInstitution(futureInstitution);
            adminEntity.getUniversities().clear();
            List<University> universities = universityRepository.findAllByInstitutionId(request.getInstitutionId());
            adminEntity.setUniversities(universities);
            adminEntity.setUser(save);
            adminEntity.setModifiedDate(LocalDateTime.now());
            adminEntityRepository.save(adminEntity);
            return new Result(ResponseMessage.SUCCESSFULLY_UPDATE.getMessage(), true);
        } catch (Exception ex) {
            return new Result(ResponseMessage.ERROR_UPDATE.getMessage(), false);
        }
    }

    @Transactional(readOnly = true)
    public Page<UAdminResponse> getUAdmins(int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<AdminEntity> allAdmins = adminEntityRepository.getAllAdmins(pageable);
        List<UAdminResponse> uAdminResponses = new ArrayList<>();
        allAdmins.forEach(adminEntity -> {
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(adminEntity.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            if (adminEntity.getFutureInstitution() != null) {
                uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
                uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            } else {
                uAdminResponse.setFutureInstId(0);
                uAdminResponse.setFutureInstName(null);
            }
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
            uAdminResponses.add(uAdminResponse);
        });
        return new PageImpl<>(uAdminResponses, pageable, allAdmins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UAdminResponse getUAdminById(int adminEntityId) {

        AdminEntity adminEntity = adminEntityRepository.getAdminById(adminEntityId).get();
        UAdminResponse uAdminResponse = new UAdminResponse();
        uAdminResponse.setId(adminEntity.getId());
        uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
        if (adminEntity.getFutureInstitution() != null) {
            uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
            uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
        } else {
            uAdminResponse.setFutureInstId(0);
            uAdminResponse.setFutureInstName(null);
        }
        uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
        return uAdminResponse;
    }

    @Transactional(readOnly = true)
    public Page<UAdminResponse> searchUAdmin(String text, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<AdminEntity> allAdmins = adminEntityRepository.searchUAdmin(text, pageable);
        List<UAdminResponse> uAdminResponses = new ArrayList<>();
        allAdmins.forEach(adminEntity -> {
            UAdminResponse uAdminResponse = new UAdminResponse();
            uAdminResponse.setId(adminEntity.getId());
            uAdminResponse.setPinfl(adminEntity.getUser().getPhoneNumber());
            if (adminEntity.getFutureInstitution() != null) {
                uAdminResponse.setFutureInstId(adminEntity.getFutureInstitution().getId());
                uAdminResponse.setFutureInstName(adminEntity.getFutureInstitution().getName());
            } else {
                uAdminResponse.setFutureInstId(0);
                uAdminResponse.setFutureInstName(null);
            }
            uAdminResponse.setUniversityResponses(adminEntity.getUniversities().stream().map(UniversityResponse::new).toList());
            uAdminResponses.add(uAdminResponse);
        });
        return new PageImpl<>(uAdminResponses, pageable, allAdmins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<GetDiplomasToExcel> getDiplomasToAdmin(String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.getAllDiplomaToAdmin(aBoolean, pageable);
        }
        return applicationRepository.getAllDiplomaNullToAdmin(pageable);
    }

    @Transactional(readOnly = true)
    public Page<GetDiplomasToExcel> getForeignDiplomasToAdmin(String status, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.getAllForeignDiplomaToAdmin(aBoolean, pageable);
        }
        return applicationRepository.getAllForeignDiplomaNullToAdmin(pageable);
    }

    @Transactional(readOnly = true)
    public Page<GetAppToExcel> getAllAppToAdmin(String appStatus, String diplomaStatus, int page, int size) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(diplomaStatus);
            return applicationRepository.getAllAppDiplomaTrueToAdmin(pageable);
        } else if (diplomaStatus.equals("null")) {
            return applicationRepository.getAllAppByDiplomaNullToAdmin(pageable);
        } else {
            return applicationRepository.getAllAppToAdmin(appStatus, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> exportDiplomasToAdmin(String status) {
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportAllDiplomaToAdmin(aBoolean);
        }
        return applicationRepository.exportAllDiplomaNullToAdmin();
    }

    @Transactional(readOnly = true)
    public List<GetDiplomasToExcel> exportForeignDiplomasToAdmin(String status) {
        if (status.equals("true") || status.equals("false")) {
            Boolean aBoolean = Boolean.valueOf(status);
            return applicationRepository.exportAllForeignDiplomaToAdmin(aBoolean);
        }
        return applicationRepository.exportAllForeignDiplomaNullToAdmin();
    }

    @Transactional(readOnly = true)
    public List<GetAppToExcel> exportAllAppToAdmin(String appStatus, String diplomaStatus) {
        if (diplomaStatus.equals("true") || diplomaStatus.equals("false")) {
            return applicationRepository.exportAllAppDiplomaTrueToAdmin();
        } else if (diplomaStatus.equals("null")) {
            return applicationRepository.exportAllAppByDiplomaNullToAdmin();
        } else {
            return applicationRepository.exportAllAppToAdmin(appStatus);
        }
    }
}
