package com.splitfinance.group.controller;

import com.splitfinance.group.dto.GroupCreateDTO;
import com.splitfinance.group.dto.GroupMemberDTO;
import com.splitfinance.group.dto.GroupResponseDTO;
import com.splitfinance.group.dto.GroupUpdateDTO;
import com.splitfinance.group.service.GroupService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<GroupResponseDTO> createGroup(@Valid @RequestBody GroupCreateDTO groupCreateDTO,
                                                        Authentication authentication) {
        String adminUsername = authentication.getName();
        GroupResponseDTO group = groupService.createGroup(adminUsername, groupCreateDTO);
        return ResponseEntity.ok(group);
    }

    // Get groups for the authenticated user (here, groups created by the user)
    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getGroups(Authentication authentication) {
        String username = authentication.getName();
        List<GroupResponseDTO> groups = groupService.getGroupsForUser(username);
        return ResponseEntity.ok(groups);
    }

    // Get details of a single group
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroupById(@PathVariable Long id) {
        GroupResponseDTO group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    // Update group details (only allowed for the admin/creator)
    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> updateGroup(@PathVariable Long id,
                                                        @Valid @RequestBody GroupUpdateDTO groupUpdateDTO,
                                                        Authentication authentication) {
        String adminUsername = authentication.getName();
        GroupResponseDTO group = groupService.updateGroup(id, adminUsername, groupUpdateDTO);
        return ResponseEntity.ok(group);
    }

    // Delete a group (only allowed for the admin/creator)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id, Authentication authentication) {
        String adminUsername = authentication.getName();
        groupService.deleteGroup(id, adminUsername);
        return ResponseEntity.ok("Group deleted successfully");
    }

    // Add a member to the group (only allowed for the admin/creator)
    @PostMapping("/{id}/members")
    public ResponseEntity<GroupResponseDTO> addMember(@PathVariable Long id,
                                                      @Valid @RequestBody GroupMemberDTO groupMemberDTO,
                                                      Authentication authentication) {
        String adminUsername = authentication.getName();
        GroupResponseDTO group = groupService.addMember(id, adminUsername, groupMemberDTO);
        return ResponseEntity.ok(group);
    }

    // Remove a member from the group (only allowed for the admin/creator)
    @DeleteMapping("/{id}/members/{memberUsername}")
    public ResponseEntity<GroupResponseDTO> removeMember(@PathVariable Long id,
                                                         @PathVariable String memberUsername,
                                                         Authentication authentication) {
        String adminUsername = authentication.getName();
        GroupResponseDTO group = groupService.removeMember(id, adminUsername, memberUsername);
        return ResponseEntity.ok(group);
    }
}
