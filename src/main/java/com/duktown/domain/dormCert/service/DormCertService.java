package com.duktown.domain.dormCert.service;

import com.duktown.domain.dormCert.dto.DormCertDto;
import com.duktown.domain.dormCert.entity.DormCert;
import com.duktown.domain.dormCert.entity.DormCertRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.CertRequestType;
import com.duktown.global.type.HallName;
import com.duktown.global.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DormCertService {
    private final UserRepository userRepository;
    private final DormCertRepository dormCertRepository;

    public void createDormCert(Long userId, DormCertDto.UserCertRequest request, MultipartFile certImg){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // TODO: AWS S3 Service 사용할 것
        // 파일 업로드(로컬 폴더)
        String absolutePath = Paths.get("").toAbsolutePath() + "/certImages";
        File localSaveFolder = new File(absolutePath);

        if (!localSaveFolder.exists()) {
            try {
                localSaveFolder.mkdir();
            } catch (Exception e) {
                throw new CustomException(CERT_IMG_FOLDER_CREATION_ERROR);
            }
        }

        String filename = certImg.getOriginalFilename();
        File destination = new File(absolutePath + "/" + filename);

        try{
            certImg.transferTo(destination);
        } catch (IOException e){
            throw new CustomException(CERT_IMG_UPLOAD_ERROR);
        }

        // 인증 요청 타입 찾기
        CertRequestType certRequestType = Arrays.stream(CertRequestType.values())
                .filter(type -> type.getValue() == request.getCertRequestType())
                .findAny().orElseThrow(() -> new CustomException(INVALID_CERT_REQUEST_TYPE_VALUE));

        // 기숙사 관명 찾기
        HallName hallName = Arrays.stream(HallName.values())
                .filter(hn -> hn.getValue() == request.getHallName())
                .findAny().orElseThrow(() -> new CustomException(INVALID_HALL_NAME_VALUE));

        DormCert dormCert = request.toEntity(user, certRequestType, hallName, destination.getPath());
        dormCertRepository.save(dormCert);
    }

    public DormCertDto.CertResponse checkDormCert(Long userId, Long dormCertId, Boolean approve){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 자기 자신 인증요청 처리 불가
        if (!user.getId().equals(userId)) {
            throw new CustomException(SELF_DORM_CERT_NOT_ALLOWED);
        }

        DormCert findDormCert = dormCertRepository.findById(dormCertId).orElseThrow(() -> new CustomException(DORM_CERT_NOT_FOUND));

        // 이미 처리된 인증요청인 경우
        if (findDormCert.getCertified() != null) {
            throw new CustomException(CERT_ALREADY_CHECKED);
        }

        // 인증요청 타입으로 사용자 권한 찾기
        RoleType roleType = Arrays.stream(RoleType.values())
                .filter(role -> role.getKey().equals(findDormCert.getCertRequestType().getRoleKey()))
                .findAny().orElseThrow(() -> new CustomException(INVALID_CERT_REQUEST_TYPE_VALUE));

        RoleType resultRoleType;

        // 승인 시
        if(approve){
            user.updateRoleType(roleType);
            userRepository.save(user);

            findDormCert.update(true);
            dormCertRepository.save(findDormCert);

            resultRoleType = user.getRoleType();
        }
        // 미승인 시
        else {
            findDormCert.update(false);
            dormCertRepository.save(findDormCert);

            resultRoleType = user.getRoleType();
        }

        return new DormCertDto.CertResponse(approve, resultRoleType);
    }
}