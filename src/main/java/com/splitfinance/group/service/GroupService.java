package com.splitfinance.group.service;

import java.util.List;

import com.splitfinance.group.dto.GroupCreateDTO;
import com.splitfinance.group.dto.GroupMemberDTO;
import com.splitfinance.group.dto.GroupResponseDTO;
import com.splitfinance.group.dto.GroupUpdateDTO;

public interface GroupService {
    GroupResponseDTO createGroup(String adminUsername, GroupCreateDTO groupCreateDTO);
    GroupResponseDTO updateGroup(Long groupId, String adminUsername, GroupUpdateDTO groupUpdateDTO);
    void deleteGroup(Long groupId, String adminUsername);
    GroupResponseDTO getGroupById(Long groupId);
    List<GroupResponseDTO> getGroupsForUser(String username);
    GroupResponseDTO addMember(Long groupId, String adminUsername, GroupMemberDTO groupMemberDTO);
    GroupResponseDTO removeMember(Long groupId, String adminUsername, String memberUsername);
}
