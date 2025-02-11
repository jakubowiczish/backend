package pl.agh.edu.master_diet.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.edu.master_diet.core.model.database.User;
import pl.agh.edu.master_diet.core.model.database.UserPlan;
import pl.agh.edu.master_diet.core.model.database.UserWeight;
import pl.agh.edu.master_diet.core.model.rest.user_plan.UserPlanResponse;
import pl.agh.edu.master_diet.core.model.shared.DemandCalculator;
import pl.agh.edu.master_diet.core.model.shared.UserParameters;
import pl.agh.edu.master_diet.exception.UserPlanNotFoundException;
import pl.agh.edu.master_diet.repository.UserPlanRepository;
import pl.agh.edu.master_diet.repository.UserRepository;
import pl.agh.edu.master_diet.repository.UserWeightRepository;
import pl.agh.edu.master_diet.service.converter.ConversionService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserPlanService {

    private final UserPlanRepository userPlanRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final DemandCalculator demandCalculator;
    private final UserWeightRepository userWeightRepository;

    public UserPlanResponse calculateAndSaveUsersPlan(UserParameters userParameters) {
        UserPlan userPlan = demandCalculator.calculateUsersPlan(userParameters);
        User user = userRepository.getOne(userParameters.getUserId());
        userPlan.setUser(user);
        updateExistingPlanOrInsertNew(user, userPlan);
        UserWeight userWeight = UserWeight.builder()
                .creationDate(LocalDateTime.now())
                .user(user)
                .weight(userParameters.getWeight())
                .build();
        userWeightRepository.save(userWeight);
        updateUserParameters(user, userParameters);
        return conversionService.convert(userPlan);
    }

    public void updateWeightInUserPlan(final Double newWeight, final Long userId) {
        User user = userRepository.getOne(userId);
        Optional<UserPlan> userPlan = userPlanRepository.findByUser(user);
        if (userPlan.isPresent()) {
            userPlan.get().setCurrentWeight(newWeight);
            userPlanRepository.save(userPlan.get());
        }
    }

    public UserPlan getUserPlan(final User user) {
        return userPlanRepository.findByUser(user)
                .orElseThrow(() -> new UserPlanNotFoundException("User plan not found"));
    }

    private void updateExistingPlanOrInsertNew(final User user, final UserPlan newUserPlan) {
        Optional<UserPlan> currentUserPlanOptional = userPlanRepository.findByUser(user);
        if (currentUserPlanOptional.isPresent()) {
            UserPlan currentUserPlan = currentUserPlanOptional.get();
            newUserPlan.setId(currentUserPlan.getId());
        }
        userPlanRepository.save(newUserPlan);
    }

    private void updateUserParameters(final User user, UserParameters userParameters) {
        user.setBirthDate(userParameters.getBirthDate());
        user.setHeight(userParameters.getHeight());
        userRepository.save(user);
    }
}
