package com.duktown.domain.unit.service;

import com.duktown.domain.unit.entity.UnitRepository;
import com.duktown.domain.unitUser.entity.UnitUserRepository;
import com.duktown.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService {

    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final UnitUserRepository unitUserRepository;

}
