package com.paypal.user_service.service;

import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WalletService walletService;

    //Constructor
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.walletService = new WalletService();
    }

    //Create User
    @Override
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        try{
            CreateWalletRequest request = new CreateWalletRequest();
            request.setUserId(savedUser.getId());
            request.setCurrency("INR");
            walletService.createWallet(request);
        } catch (Exception e) {
            userRepository.deleteById(savedUser.getId());
            throw new RuntimeException("Failed to create wallet for user. User creation rolled back.", e);
        }
        return savedUser;
    }

    //Get user by ID
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
