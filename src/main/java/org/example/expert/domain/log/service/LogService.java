package org.example.expert.domain.log.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;


    // 정상 로그 저장
    public void saveLog(String message) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        logRepository.save(Log.of(ip, LocalDateTime.now(), message));
    }

    // 에러 로그 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLogWithError(String errorMessage) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        logRepository.save(Log.of(ip, LocalDateTime.now(), "Error: " + errorMessage));
    }
}
