package com.example.demo.domain.card.service;

import com.example.demo.domain.card.entity.Card;
import com.example.demo.domain.card.repository.CardRepository;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    //카드 장수로 인한 인원수 문제가 있었다(이 메서드에서 인원수에 따른 카드수 조정을 하면 해결될듯)
    public ResponseEntity<?> check(User user) {  //처음에만 보여주는 용도로 쓰는건가? 아니면 그 뒤에도 사용되나? 확인해보고 두 경우를 분리조치할것
        /*int deckSize = 53;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < deckSize; ++i) {
            list.add(i);
        }
        Collections.shuffle(list);


        Card card = new Card();*/

        String userId = user.getUserId();
        List<Card> cards = cardRepository.findCardsByUserUserId(userId);

        //       ArrayList<Card> cards = userRepository.findCardsByUserId(userId);

        return null;
    }

    //전체 카드 보여줌 -> 중복 제거 -> 게임 시작(이 사이 딜레이와 배분과정은 어디서 보여줄거지?
    //아마도 룸에 들어가야될듯? start메서드에?
    //숫자만 보내줘도 프론트에서 특정숫자와 트럼프카드 그림을 연동시킬수 있을까?
    public ResponseEntity<?> startDistinct(String roomId) {

        Room room = roomRepository.findByRoomId(roomId);
        int player = Math.toIntExact(room.getHeadCount());
        int extra = 0;

        if (3 > player || player > 6) {
            return ResponseEntity.badRequest().build();
        }

        List<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 4; j++) {
                deck.add(i);
            }
        }
        deck.add(99);
        Collections.shuffle(deck);

        // 덱에서 카드를 뽑아 플레이어에게 배분
        List<List<Integer>> playerHands = new ArrayList<>();
        int cardsPerPlayer = deck.size() / player;
        for (int i = 0; i < player; i++) {
            List<Integer> playerHand = new ArrayList<>();
            for (int j = 0; j < cardsPerPlayer; j++) {
                playerHand.add(deck.remove(0));
            }
            playerHands.add(playerHand);
        }

        while (!deck.isEmpty()) {
            playerHands.get(extra).add(deck.remove(0));
            extra++;
        }

//        for (List<Integer> hand : playerHands) {
//
//            Map<Integer, Integer> countMap = new HashMap<>();
//
//            // 각 숫자의 개수를 세기
//            for (int number : hand) {
//                countMap.put(number, countMap.getOrDefault(number, 0) + 1);
//            }
//
//            List<Integer> uniqueNumbers = new ArrayList<>();
//            for (int number : hand) {
//                int count = countMap.get(number);
//                if (count % 2 == 1) {
//                    if(!uniqueNumbers.contains(number)) {
//                        uniqueNumbers.add(number);
//                    }
//                }
//            }
//        }

        return null;
    }

    //내 손패 중복제거
    public List<Integer> distinct(List<Integer> playerHands) {

        Map<Integer, Integer> countMap = new HashMap<>();

        for (Integer hand : playerHands) {
            countMap.put(hand, countMap.getOrDefault(hand, 0) + 1);
        }

        List<Integer> uniqueNumbers = new ArrayList<>();
        for (int number : playerHands) {
            int count = countMap.get(number);
            if (count % 2 == 1 && !uniqueNumbers.contains(number)) {
                uniqueNumbers.add(number);
            }
        }

        return uniqueNumbers;
    }

    //카드 뽑기(나중에 쓸려나)    //지워질듯? 확인하기
    public ResponseEntity<?> pick(Card card, User user) {    //내가 뽑을 대상의 카드
        List<Card> cards = cardRepository.findCardsByUserUserId(user.getUserId());
        cards.add(card);

        return null;
    }

    //뽑기랑 중복제거 같이 쓰네 //카드 뽑기 & 카드 중복 제거 & 종료된 사람 순서 스킵 & 종료 & 경험치
    public ResponseEntity<?> distinct(Card card, User user) {
        //카드 뽑아서 카드 중복 제거
        List<Card> cards = cardRepository.findCardsByUserUserId(user.getUserId());
        cards.add(card);
        List<Card> restCards = new ArrayList<>(new HashSet<>(cards));   //테스트해보기

        //종료된 사람
        if (restCards == null) {
            user.setDone();

            //게임 끝난 여부 확인       //방을 userid로 왜 찾지? 그냥 roomid 받아오자 체크해두기
            //유저가 끝날때마다 매번 모든 유저의 done 값을 확인하는건 비효율적인거같다. 끝날때마다 count값을 올려서 메모리같은곳에 저장할수 없나?
            Room room = userRepository.findRoomByUserId(user.getUserId());
            List<User> users = userRepository.findUsersByRoomRoomId(room.getRoomId());
            Long count = 0L;
            for (User u : users) {
                if (!u.isDone()) {
                    count++;
                }
            }
            //게임 종료
            if (count == (room.getHeadCount() - 1L)) {
                return ResponseEntity.ok("게임종료");
            }
        }

        return null;
    }

    //카드를 보내주는게 나을까 userid만 받아서 우리가 찾는게 나을까?
    public ResponseEntity<?> shuffle(String userId) {
        List<Card> cards = cardRepository.findCardsByUserUserId(userId);
        Collections.shuffle(cards);

        return null;
    }
}
