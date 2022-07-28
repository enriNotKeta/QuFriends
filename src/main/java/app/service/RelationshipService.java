package app.service;

import app.model.Relationship;
import app.model.User;
import app.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserService userService;


    @Autowired
    public RelationshipService(RelationshipRepository relationshipRepository,UserService userService) {
        this.relationshipRepository = relationshipRepository;
        this.userService = userService;

    }

    public List<User> getMatches(Long userId) {
        List<Relationship> relationships = relationshipRepository.getMatches(userId);
        System.out.println("rels like " + relationships);
        List<User> usersMatchedWith = new ArrayList<>();
        Long currentUserId = userService.getCurrentUser().getId();

        for (Relationship relationship : relationships) {
            if (!usersMatchedWith.contains(relationship.getUserA()) && !usersMatchedWith.contains(relationship.getUserB())) {
                if (currentUserId != relationship.getUserA().getId()) {
                    usersMatchedWith.add(relationship.getUserA());
                } else if (currentUserId != relationship.getUserB().getId()) {
                    usersMatchedWith.add(relationship.getUserB());
                }
            }
        }

        return usersMatchedWith;
    }

    public List<Relationship> findAll() {
        List<Relationship> relationships = new ArrayList<>();
        relationshipRepository.findAll().forEach(relationships::add);
        return relationships;
    }

    public Relationship umatchUsers(Long userIdToUnmatch) throws ResourceNotFoundException {
        User userToUnmatch = userService.getUserById(userIdToUnmatch);
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userToUnmatch, userService.getCurrentUser());
        if (relationship != null) {
            System.out.println(relationship.getUserA().getEmail() + ", relasss");

            relationship.setUserAUnmatchesUserB(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);
        }
        else {
            relationship = relationshipRepository.findByUserBAndUserA(userToUnmatch, userService.getCurrentUser());

            if (relationship == null) {
                throw new ResourceNotFoundException("Relationship of users with id: " + userIdToUnmatch + ", "
                        + userService.getCurrentUser().getId() + " not found");
            }
            System.out.println(relationship.getUserB().getEmail() + ", relasss");

            relationship.setUserBUnmatchesUserA(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);

        }
        relationshipRepository.save(relationship);
        return relationship;
    }


    public Relationship blockUser(Long userIdToUnmatch) throws ResourceNotFoundException {
        User userToBlock = userService.getUserById(userIdToUnmatch);
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userToBlock, userService.getCurrentUser());
        if (relationship != null) {
            System.out.println(relationship.getUserA().getEmail() + ", relasss");

            relationship.setUserABlocksUserB(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);
        }
        else {
            relationship = relationshipRepository.findByUserBAndUserA(userToBlock, userService.getCurrentUser());

            if (relationship == null) {
                throw new ResourceNotFoundException("Relationship of users with id: " + userIdToUnmatch + ", "
                        + userService.getCurrentUser().getId() + " not found");
            }
            System.out.println(relationship.getUserB().getEmail() + ", relasss");

            relationship.setUserBBlocksUserA(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);

        }
        relationshipRepository.save(relationship);
        return relationship;
    }



}
