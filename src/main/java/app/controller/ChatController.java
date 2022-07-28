package app.controller;

import app.model.ChatMessage;
import app.model.Relationship;
import app.service.RelationshipService;
import app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import static java.lang.String.format;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final UserService userService;
    private final RelationshipService relationshipService;


    @Autowired
    public ChatController(UserService userService, RelationshipService relationshipService){
        this.userService = userService;
        this.relationshipService = relationshipService;

    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    }

    @MessageMapping("/chat/{roomId}/addUser")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
                        SimpMessageHeaderAccessor headerAccessor) {
        String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        if (currentRoomId != null) {
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setType(ChatMessage.MessageType.LEAVE);
            leaveMessage.setSender(chatMessage.getSender());
            messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
        }
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    }

    @GetMapping(value = "/chat/{chatterId}/{chattedId}")
    public String showChat(Model model, @PathVariable long chatterId, @PathVariable long chattedId) {
        if (userService.getCurrentUser().getId() == chatterId || userService.getCurrentUser().getId() == chattedId) {
            System.out.println(userService.getCurrentUser().getUsername() + ", usernameCurrUser");

            Relationship relationship1 = relationshipService.findByUserAAndUserB(chatterId, chattedId);
            Relationship relationship2 = relationshipService.findByUserBAndUserA(chatterId, chattedId);
            if (relationship1 != null || relationship2 != null) {
                model.addAttribute("chatterUserName", userService.getCurrentUser().getUsername());

                if (relationship1 != null) {
                    model.addAttribute("roomId", relationship1.getChatRoomId());
                }
                else if (relationship2 != null){
                    model.addAttribute("roomId", relationship2.getChatRoomId());
                }
                return "index";

            }
        }

        return "redirect:/home";
    }
}
