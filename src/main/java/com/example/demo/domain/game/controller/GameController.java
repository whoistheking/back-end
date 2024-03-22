package com.example.demo.domain.game.controller;

import com.example.demo.domain.card.dto.CardMessageDto;
import com.example.demo.domain.card.dto.CardUserResponseDto;
import com.example.demo.domain.card.dto.CardsResponseDto;
import com.example.demo.domain.card.repository.CardRepository;
import com.example.demo.domain.card.service.CardService;
import com.example.demo.domain.room.dto.GameMessageDto;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room.service.RoomService;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class GameController {
    private final UserRepository userRepository;
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final RabbitTemplate rabbitTemplate;

    //        private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("game.{roomId}")
    public void gameMessageProxy(@DestinationVariable("roomId") String roomId, @Payload GameMessageDto message) throws JsonProcessingException {
        System.out.println("여기에 들어오나 메시지매핑 메서드");
        if (GameMessageDto.MessageType.START.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            gameStarter(roomId, message);
        }
        if (GameMessageDto.MessageType.READY.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            gameReady(message);
        }

        if (GameMessageDto.MessageType.ENDGAME.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            gameEnd(message);
        }
//        if (GameMessageDto.MessageType.CREATE.equals(message.getType())) {
//            System.out.println("여기에 들어오나" + message.getType());
//            roomCreate(message);
//        }
        if (GameMessageDto.MessageType.JOIN.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            roomJoin(roomId, message);
        }
        if (GameMessageDto.MessageType.LEAVE.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            roomLeave(message);
        }
        if (GameMessageDto.MessageType.MATCHING.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            roomMatching(message);
        }

    }

    @MessageMapping("/card.{roomId}")
    public void cardMessageProxy(@DestinationVariable("roomId") String roomId, @Payload CardMessageDto message) throws JsonProcessingException {
        System.out.println("여기에 들어오나 메시지매핑 메서드");

        if (CardMessageDto.MessageType.DEAL.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            cardDeal(roomId, message);
        }
        if (CardMessageDto.MessageType.ENDTURN.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            turnEnd(roomId, message);
        }
        if (CardMessageDto.MessageType.CHECK.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            cardCheck(roomId, message);
        }
        if (CardMessageDto.MessageType.DISTINCT.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            cardDistinct(roomId, message);
        }
        if (CardMessageDto.MessageType.PICK.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            cardPick(roomId, message);
        }
        if (CardMessageDto.MessageType.SHUFFLE.equals(message.getType())) {
            System.out.println("여기에 들어오나" + message.getType());
            cardShuffle(roomId, message);
        }
    }

    public void gameStarter(String roomId, GameMessageDto message) throws JsonProcessingException {
        System.out.println("여기에 들어오나 게임스타터");
        roomService.start(message.getRoomId());
        Room room = roomRepository.findByRoomId(message.getRoomId());

        List<User> users = userRepository.findUsersByRoomRoomId(roomId);

        Long checkReady = 0L;
        for (User user : users) {
            if (!user.isReady()) {
                checkReady++;
            }
        }
        if (!(checkReady == room.getHeadCount())) {
            return; //에러로 빼?
        }

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.START);
        gameMessage.setContent("방에 입장하였습니다");
        rabbitTemplate.convertAndSend("game.exchange", "start.room." + roomId, message);

//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }   //주석된 부분은 알맞게 수정해주세요.

    //전원 준비완료한지 확인 후 게임시작 버튼 활성화
    private void gameReady(GameMessageDto message) {
        System.out.println("여기에 들어오나 게임레디");
        String userId = message.getSender();
        String roomId = message.getRoomId();
        User user = userRepository.findByUserId(message.getSender());
        Room room = roomRepository.findByRoomId(roomId);

        user.setReady();

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.READY);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    private void cardDeal(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 카드 배분");

        CardUserResponseDto cardUserResponseDto = cardService.startDistinct(message.getRoomId());

//        String userId = message.getSender();
//        List<Card> cards = cardRepository.findCardsByUserUserId(userId);
//        if (cards.isEmpty()) {
//            User user = userRepository.findByUserId(userId);
//            user.setDone();
//        }

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.DEAL);
        rabbitTemplate.convertAndSend("game.exchange", "deal.room." + roomId, cardUserResponseDto);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //필요하나?
    //message 바꿨는데 뭐가 나은지 나중에 프론트한테 물어보기
    private void turnEnd(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 플레이어 끝");

        String userId = message.getSender();
        ResponseEntity<?> response = cardService.PlayerDone(roomId, userId);

//        List<Card> cards = cardRepository.findCardsByUserUserId(userId);
//        if (cards.isEmpty()) {
//            User user = userRepository.findByUserId(userId);
//            user.setDone();
//        }

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.ENDTURN);
        rabbitTemplate.convertAndSend("game.exchange", "turn.room." + roomId, response);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //경험치 추가 안했음
    private void gameEnd(GameMessageDto message) {
        System.out.println("여기에 들어오나 게임 끝");
        String roomId = message.getRoomId();
        Room room = roomRepository.findByRoomId(roomId);
        List<User> users = userRepository.findUsersByRoomRoomId(message.getRoomId());
        Long count = 0L;
        for (User u : users) {
            if (!u.isDone()) {
                count++;
            }
        }
        //게임 종료
        if (!(count == (room.getHeadCount() - 1L))) {
            return;    //에러? 아니면 다른 방법?
        }
//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.ENDGAME);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //안쓸거 같은데
    private void roomCreate(GameMessageDto message) {
        System.out.println("여기에 들어오나 방 생성");
        User user = userRepository.findByUserId(message.getSender());
        String uuid = UUID.randomUUID().toString();
        Room room = new Room(uuid, user.getUserId(), false);
        roomRepository.save(room);

        user.joinUser(room);
        userRepository.save(user);

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.CREATE);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    private void roomJoin(String roomId, GameMessageDto message) {
        System.out.println("여기에 들어오나 방 입장");
        Room room = roomRepository.findByRoomId(message.getRoomId());
        User user = userRepository.findByUserId(message.getSender());

        user.joinUser(room);
        room.joinRoom();

        userRepository.save(user);
        roomRepository.save(room);

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.JOIN);

        rabbitTemplate.convertAndSend("game.exchange", "join.room." + roomId, message);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    private void roomLeave(GameMessageDto message) {
        System.out.println("여기에 들어오나 방 떠나기");
        String roomId = message.getRoomId();
        User user = userRepository.findByUserId(message.getSender());
        Room room = roomRepository.findByRoomId(roomId);

        room.leaveRoom();
        if (room.getHeadCount() == 0) {
            roomRepository.deleteRoomByRoomId(roomId);
        }
        if (!(room == null)) {
            int randomManager = (int) (Math.random() * room.getHeadCount());
            List<User> users = userRepository.findUsersByRoomRoomId(roomId);
            room.changeManager(users.get(randomManager).getUserId());
        }
        user.leave();

        roomRepository.save(room);
        userRepository.save(user);

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(message.getRoomId());
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.LEAVE);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //필요한가?
    private void roomMatching(GameMessageDto message) {
        System.out.println("여기에 들어오나 방 자동 매칭");
        String roomId = roomRepository.findRoomIdByCreatedAsc();
        if (roomId == null) {
            User user = userRepository.findByUserId(message.getSender());
            String uuid = UUID.randomUUID().toString();
            Room room = new Room(uuid, user.getUserId(), true);
            roomRepository.save(room);

            user.joinUser(room);
            userRepository.save(user);
        }

//        String messageContent = jsonStringBuilder.gameStarter(game);
        GameMessageDto gameMessage = new GameMessageDto();
        gameMessage.setRoomId(roomId);
        gameMessage.setSender(message.getSender());
        gameMessage.setType(GameMessageDto.MessageType.MATCHING);
        rabbitTemplate.convertAndSend("game.exchange", "start.room." + roomId, message);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //cards를 보는 용도인데 수정이 필요함
    private void cardCheck(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 내 카드 조회");

        CardsResponseDto cardsResponseDto = cardService.check(message.getSender());

//        User user = userRepository.findByUserId(message.getSender());
//        String userId = user.getUserId();
//        List<Card> cards = cardRepository.findCardsByUserUserId(userId);

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.CHECK);
        rabbitTemplate.convertAndSend("game.exchange", "check.room." + roomId, cardsResponseDto);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //중복 제거 수정 필요
    private void cardDistinct(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 카드 중복 제거");

        List<Integer> cards = cardService.distinct(message.getSender());

//        List<Integer> playerHands = cardRepository.findCardNumByUserUserId(message.getSender());
//        Map<Integer, Integer> countMap = new HashMap<>();
//
//        for (Integer hand : playerHands) {
//            countMap.put(hand, countMap.getOrDefault(hand, 0) + 1);
//        }
//
//        List<Integer> uniqueNumbers = new ArrayList<>();
//        for (int number : playerHands) {
//            int count = countMap.get(number);
//            if (count % 2 == 1 && !uniqueNumbers.contains(number)) {
//                uniqueNumbers.add(number);
//            }
//        }

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.DISTINCT);
        rabbitTemplate.convertAndSend("game.exchange", "distinct.room." + roomId, cards);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //상대방 카드 뽑기 수정 필요
    //다음 사람 userid값 필요, 만약 프론트에서 못준다고 하면 cardDeal에서 순서 속성추가하고 저장
    private void cardPick(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 상대 카드 뽑기");

        CardsResponseDto cardsResponseDto = cardService.pick(message.getSender(), message.getNextUser(), message.getNextCardNum());
//        List<Card> cards = cardRepository.findCardsByUserUserId(message.getSender()); //틀렸음
//        cards.add(card);    //카드 어디로 반환해?

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.PICK);
        rabbitTemplate.convertAndSend("game.exchange", "pick.room." + roomId, cardsResponseDto);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }

    //수정 필요
    //turnEnd처럼 물어보고 변경하기
    private void cardShuffle(String roomId, CardMessageDto message) {
        System.out.println("여기에 들어오나 내 카드 섞기");

        ResponseEntity response = cardService.shuffle(message.getSender());

//        List<Card> cards = cardRepository.findCardsByUserUserId(message.getSender());
//        Collections.shuffle(cards);

//        String messageContent = jsonStringBuilder.gameStarter(game);
        CardMessageDto cardMessage = new CardMessageDto();
        cardMessage.setRoomId(message.getRoomId());
        cardMessage.setSender(message.getSender());
        cardMessage.setType(CardMessageDto.MessageType.SHUFFLE);
        rabbitTemplate.convertAndSend("game.exchange", "shuffle.room." + roomId, response);
//        gameMessage.setContent(messageContent);
//        messagingTemplate.convertAndSend("/sub/game/" + message.getRoomId(), gameMessage);
    }
}
