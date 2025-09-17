package com.paypal.reward_service.Controller;

import com.paypal.reward_service.entity.Reward;
import com.paypal.reward_service.repository.rewardRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rewards/")
public class rewardsController {

    private final rewardRepository rewardRepository;

    public rewardsController(rewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/rewards")
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Reward> getRewardsByUserId(@PathVariable Long userId) {
        return rewardRepository.findByUserId(userId);
    }
}
