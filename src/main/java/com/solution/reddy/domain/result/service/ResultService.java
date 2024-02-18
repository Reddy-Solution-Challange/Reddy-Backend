package com.solution.reddy.domain.result.service;

import com.solution.reddy.domain.result.dto.SaveCheckResultRequest;
import com.solution.reddy.domain.result.entity.ResultEntity;
import com.solution.reddy.domain.result.entity.ResultGroupEntity;
import com.solution.reddy.domain.result.entity.ResultPostEntity;
import com.solution.reddy.domain.result.resository.ResultGroupRepository;
import com.solution.reddy.domain.result.resository.ResultPostRepository;
import com.solution.reddy.domain.result.resository.ResultRepository;
import com.solution.reddy.domain.user.entity.UserEntity;
import com.solution.reddy.domain.user.repository.UserRepository;
import com.solution.reddy.global.exception.ApiException;
import com.solution.reddy.global.message.ResultGroupMessage;
import com.solution.reddy.global.message.ResultMessage;
import com.solution.reddy.global.message.UserMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final ResultPostRepository resultPostRepository;
    private final ResultGroupRepository resultGroupRepository;
    private final UserRepository userRepository;

    public void saveCheckResult(SaveCheckResultRequest request, String email) {
        ResultGroupEntity groupEntity;
        if(request.groupId() == 0) {
            groupEntity = resultGroupRepository.save(
                    ResultGroupEntity.builder()
                            .user(findUserByEmail(email))
                            .build()
            );
        } else {
            Optional<ResultGroupEntity> findGroup = resultGroupRepository.findById(request.groupId());
            if(findGroup.isEmpty()) {
                throw new ApiException(ResultGroupMessage.RESULT_GROUP_NOT_FOUND);
            }
            groupEntity = findGroup.get();
        }

        Optional<ResultEntity> resultEntity = resultRepository.findById(request.resultId());
        if (resultEntity.isEmpty()) {
            throw new ApiException(ResultMessage.RESULT_NOT_FOUND);
        }

        resultPostRepository.save(
                ResultPostEntity.builder()
                        .group(groupEntity)
                        .result(resultEntity.get())
                        .imageUrl(request.imageUrl())
                        .build());
    }

    private UserEntity findUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        }
        return user.get();
    }
}
