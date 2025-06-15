package com.splitfinance.group.service;

import com.splitfinance.auth.entity.User;
import com.splitfinance.auth.repository.UserRepository;
import com.splitfinance.group.dto.GroupCreateDTO;
import com.splitfinance.group.dto.GroupMemberDTO;
import com.splitfinance.group.dto.GroupResponseDTO;
import com.splitfinance.group.dto.GroupUpdateDTO;
import com.splitfinance.group.entity.UserGroup;
import com.splitfinance.group.repository.UserGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupServiceImpl(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GroupResponseDTO createGroup(String adminUsername, GroupCreateDTO groupCreateDTO) {
        UserGroup group = new UserGroup();
        group.setGroupName(groupCreateDTO.getGroupName());
        group.setDescription(groupCreateDTO.getDescription());
        group.setCreatedAt(LocalDateTime.now());
        group.setCreatedBy(adminUsername);

        // Add the admin (creator) as a member by default.
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().add(adminUser);

        UserGroup savedGroup = userGroupRepository.save(group);
        return mapToGroupResponseDTO(savedGroup);
    }

    @Override
    public GroupResponseDTO updateGroup(Long groupId, String adminUsername, GroupUpdateDTO groupUpdateDTO) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getCreatedBy().equals(adminUsername)) {
            throw new RuntimeException("You are not authorized to update this group");
        }
        group.setGroupName(groupUpdateDTO.getGroupName());
        group.setDescription(groupUpdateDTO.getDescription());
        UserGroup updatedGroup = userGroupRepository.save(group);
        return mapToGroupResponseDTO(updatedGroup);
    }

    @Override
    public void deleteGroup(Long groupId, String adminUsername) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getCreatedBy().equals(adminUsername)) {
            throw new RuntimeException("You are not authorized to delete this group");
        }
        userGroupRepository.delete(group);
    }

    @Override
    public GroupResponseDTO getGroupById(Long groupId) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return mapToGroupResponseDTO(group);
    }

    @Override
    public List<GroupResponseDTO> getGroupsForUser(String username) {
        // Option A: Get groups where the user is the creator.
        List<UserGroup> groups = userGroupRepository.findByCreatedBy(username);
        // For more complete behavior, you might add a custom query to fetch groups in which the user is a member.
        return groups.stream()
                .map(this::mapToGroupResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponseDTO addMember(Long groupId, String adminUsername, GroupMemberDTO groupMemberDTO) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getCreatedBy().equals(adminUsername)) {
            throw new RuntimeException("You are not authorized to update this group");
        }
        User userToAdd = userRepository.findByUsername(groupMemberDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().add(userToAdd);
        UserGroup updatedGroup = userGroupRepository.save(group);
        return mapToGroupResponseDTO(updatedGroup);
    }

    @Override
    public GroupResponseDTO removeMember(Long groupId, String adminUsername, String memberUsername) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getCreatedBy().equals(adminUsername)) {
            throw new RuntimeException("You are not authorized to update this group");
        }
        User userToRemove = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().remove(userToRemove);
        UserGroup updatedGroup = userGroupRepository.save(group);
        return mapToGroupResponseDTO(updatedGroup);
    }
    
    private GroupResponseDTO mapToGroupResponseDTO(UserGroup group) {
        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setId(group.getId());
        dto.setGroupName(group.getGroupName());
        dto.setDescription(group.getDescription());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setCreatedBy(group.getCreatedBy());
        dto.setMemberUsernames(
                group.getMembers().stream()
                     .map(User::getUsername)
                     .collect(Collectors.toSet())
        );
        return dto;
    }
}
