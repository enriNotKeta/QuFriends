package app.service;

import app.exception.BadResourceException;
import app.exception.ResourceAlreadyExistsException;
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
            //ordered, no dubs, not itself, could b done w query
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
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userService.getCurrentUser(), userToUnmatch);
        if (relationship != null) {
            System.out.println(relationship.getUserA().getEmail() + ", relasss");

            relationship.setUserAUnmatchesUserB(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);
        }
        else {
            relationship = relationshipRepository.findByUserBAndUserA(userService.getCurrentUser(), userToUnmatch);

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
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userService.getCurrentUser(), userToBlock);
        if (relationship != null) {
            System.out.println(relationship.getUserA().getEmail() + ", relasss");

            relationship.setUserABlocksUserB(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);
        }
        else {
            relationship = relationshipRepository.findByUserBAndUserA(userService.getCurrentUser(), userToBlock);

            if (relationship == null) {
                relationship = new Relationship();
                relationship.setUserB(userService.getCurrentUser());
                relationship.setUserA(userToBlock);
            }

            relationship.setUserBBlocksUserA(true);
            relationship.setUserALikesUserB(false);
            relationship.setUserBLikesUserA(false);

        }
        relationshipRepository.save(relationship);
        return relationship;
    }


    public Relationship requestUser(Long userIdToRequest) throws ResourceAlreadyExistsException {
        User userToRequest = userService.getUserById(userIdToRequest);
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userToRequest, userService.getCurrentUser());

        if (relationship != null) {
            throw new ResourceAlreadyExistsException("Relationship of users with id: " + userIdToRequest + ", "
                    + userService.getCurrentUser().getId() + " already exists");
        }
        else {
            relationship = relationshipRepository.findByUserBAndUserA(userToRequest, userService.getCurrentUser());

            if (relationship != null) {
                throw new ResourceAlreadyExistsException("Relationship of users with id: " + userService.getCurrentUser().getId() + ", "
                        + userIdToRequest + " already exists");
            }
        }

        //userA will b the requester
        relationship = new Relationship();
        relationship.setUserA(userService.getCurrentUser());
        relationship.setUserB(userToRequest);
        relationship.setUserALikesUserB(true);
        relationshipRepository.save(relationship);
        return relationship;
    }


    public Relationship acceptRequest(Long userIdRequester) throws BadResourceException {
        User userRequester = userService.getUserById(userIdRequester);
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userRequester, userService.getCurrentUser());

        if (relationship == null) {
            throw new BadResourceException("Relationship of users with id: " + userIdRequester + ", "
                    + userService.getCurrentUser().getId() + " doesnt exist");
        }
        relationship.setUserBLikesUserA(true);
        relationshipRepository.save(relationship);
        return relationship;
    }

    public Relationship findByUserAAndUserB(Long userAId, Long userBId){
        User userA = userService.getUserById(userAId);
        User userB = userService.getUserById(userBId);
        Relationship relationship = relationshipRepository.findByUserAAndUserB(userA, userB);
        return relationship;
    }

    public Relationship findByUserBAndUserA(Long userAId, Long userBId) {
        User userA = userService.getUserById(userAId);
        User userB = userService.getUserById(userBId);
        Relationship relationship = relationshipRepository.findByUserBAndUserA(userA, userB);

        return relationship;
    }







}
