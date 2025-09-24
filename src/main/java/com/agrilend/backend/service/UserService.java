package com.agrilend.backend.service;

import com.agrilend.backend.dto.user.UserProfileDto;
import com.agrilend.backend.entity.Buyer;
import com.agrilend.backend.entity.Farmer;
import com.agrilend.backend.entity.User;
import com.agrilend.backend.entity.enums.UserRole;
import com.agrilend.backend.repository.BuyerRepository;
import com.agrilend.backend.repository.FarmerRepository;
import com.agrilend.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        UserProfileDto dto = modelMapper.map(user, UserProfileDto.class);

        if (user.getRole() == UserRole.FARMER) {
            Farmer farmer = farmerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("Profil Agriculteur non trouvé pour l'utilisateur: " + userId));
            dto.setFarmName(farmer.getFarmName());
            dto.setFarmLocation(farmer.getFarmLocation());
            dto.setFarmSize(farmer.getFarmSize());
        } else if (user.getRole() == UserRole.BUYER) {
            Buyer buyer = buyerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("Profil Acheteur non trouvé pour l'utilisateur: " + userId));
            dto.setCompanyName(buyer.getCompanyName());
            dto.setCompanyAddress(buyer.getCompanyAddress());
            dto.setActivityType(buyer.getActivityType());
        }

        return dto;
    }

    public UserProfileDto updateUserProfile(Long userId, UserProfileDto userProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setPhone(userProfileDto.getPhone());
        userRepository.save(user);

        if (user.getRole() == UserRole.FARMER) {
            Farmer farmer = farmerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("Profil Agriculteur non trouvé pour l'utilisateur: " + userId));
            farmer.setFarmName(userProfileDto.getFarmName());
            farmer.setFarmLocation(userProfileDto.getFarmLocation());
            farmer.setFarmSize(userProfileDto.getFarmSize());
            farmerRepository.save(farmer);
        } else if (user.getRole() == UserRole.BUYER) {
            Buyer buyer = buyerRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("Profil Acheteur non trouvé pour l'utilisateur: " + userId));
            buyer.setCompanyName(userProfileDto.getCompanyName());
            buyer.setCompanyAddress(userProfileDto.getCompanyAddress());
            buyer.setActivityType(userProfileDto.getActivityType());
            buyerRepository.save(buyer);
        }

        return getUserProfile(userId);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserProfileDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapUserToProfileDto);
    }

    @Transactional(readOnly = true)
    public Page<UserProfileDto> getUsersByRole(UserRole role, Pageable pageable) {
        Page<User> users = userRepository.findByRole(role, pageable);
        return users.map(this::mapUserToProfileDto);
    }

    private UserProfileDto mapUserToProfileDto(User user) {
        return modelMapper.map(user, UserProfileDto.class);
    }
}