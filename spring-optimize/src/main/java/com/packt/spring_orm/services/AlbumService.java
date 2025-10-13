package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.AlbumEntity;
import com.packt.spring_orm.entities.CardEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.entities.UserEntity;
import com.packt.spring_orm.mappers.PlayerMapper;
import com.packt.spring_orm.records.*;
import com.packt.spring_orm.repositories.AlbumRepository;
import com.packt.spring_orm.repositories.CardRepository;
import com.packt.spring_orm.repositories.PlayerRepository;
import com.packt.spring_orm.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AlbumService {

  private final static Logger log = LoggerFactory.getLogger(AlbumService.class);

  private final AlbumRepository albumRepository;
  private final UserRepository userRepository;
  private final CardRepository cardRepository;
  private final PlayerRepository playerRepository;
  private final PlayerMapper playerMapper;

  public AlbumService(AlbumRepository albumRepository,  UserRepository userRepository, CardRepository cardRepository, PlayerRepository playerRepository) {
    this.albumRepository = albumRepository;
    this.userRepository = userRepository;
    this.cardRepository = cardRepository;
    this.playerRepository = playerRepository;
    this.playerMapper = new PlayerMapper();
  }

  @Transactional
  public Album buyAlbum (Long userId, String title) {
    AlbumEntity albumEntity = new AlbumEntity();
    albumEntity.setTitle(title);
    albumEntity.setExpireDate(LocalDate.now().plusYears(1));
    albumEntity.setOwner(userRepository.findById(userId).orElseThrow());
    albumEntity = albumRepository.save(albumEntity);
    return new Album(albumEntity.getId(), albumEntity.getTitle(), albumEntity.getOwner().getId());
  }

  @Transactional
  public List<Card> buyCards(Long userId, Integer count) {
    Random random = new Random();
    List<PlayerEntity> players = getAvailablePlayers();
    if (players.isEmpty()) {
      throw new IllegalStateException("No available players to assign to cards.");
    }
    Optional<UserEntity> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      log.error("User with id " + userId + " not found.");
      throw new IllegalArgumentException("User with id " + userId + " not found.");
    }
    UserEntity userEntity = userOpt.get();
    List<CardEntity> cardEntities = Stream.generate(() -> {
      CardEntity cardEntity = new CardEntity();
      cardEntity.setOwner(userEntity);
      cardEntity.setPlayer(players.get(random.nextInt(players.size())));
      return cardEntity;
    }).limit(count).toList();
    return cardRepository.saveAll(cardEntities).stream().map(cardEntity -> new Card(cardEntity.getId(), cardEntity.getOwner().getId(), Optional.empty(),
            playerMapper.map(cardEntity.getPlayer())))
        .collect(Collectors.toList());
  }

  @Cacheable(value = "availablePlayers")
  public List<PlayerEntity> getAvailablePlayers() {
    return playerRepository.findAll();
  }

  @Transactional
  public Optional<Card> transferCard(Long cardId, Long userId) {
    Integer count = cardRepository.transferCard(cardId, userId);
    if (count == 0) {
      return Optional.empty();
    } else {
      CardEntity card = cardRepository.findById(cardId).orElseThrow();
      if (card.getOwner()==null) {
        throw new RuntimeException("New owner not found");
      }
      return Optional.of(new Card(card.getId(), card.getOwner().getId(),
          card.getAlbum() == null ? Optional.empty() : Optional.of(card.getAlbum().getId()),
          playerMapper.map(card.getPlayer())));
    }
  }

  public Card addCardToAlbum(Long cardId, Long albumId) {
    CardEntity card = cardRepository.findById(cardId).orElseThrow();
    AlbumEntity album = albumRepository.findById(albumId).orElseThrow();
    card.setAlbum(album);
    card = cardRepository.save(card);
    return new Card(card.getId(), card.getOwner().getId(), Optional.of(card.getAlbum().getId()),
        playerMapper.map(card.getPlayer()));

  }

  /*
   * Take all non used cards of the user and assign to the albums of the user
   */
  @Transactional
  public List<Card> useAllCardAvailable(Long userId) {
    // UserEntity user = usersRepository.findById(userId).orElseThrow();
    // List<CardEntity> cards = cardsRepository.findAllByOwnerAndAlbumIsNull(user);
    // List<AlbumEntity> albums = albumsRepository.findAllByOwner(user);
    // for (int i = 0; i < cards.size(); i++) {
    // cards.get(i).setAlbum(albums.get(i % albums.size()));
    // }
    // cardsRepository.saveAll(cards);
    return cardRepository.assignCardsToUserAlbums(userId)
        .stream()
        .map(c -> new Card(c.getId(), c.getOwner().getId(), Optional.of(c.getAlbum().getId()),
            playerMapper.map(c.getPlayer())))
        .toList();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public List<Card> tradeAllCards(Long userId1, Long userId2) {
    Integer potentialUser1ToUser2 = cardRepository.countMatchBetweenUsers(userId1, userId2);
    Integer potentialUser2ToUser1 = cardRepository.countMatchBetweenUsers(userId2, userId1);
    Integer count = Math.min(potentialUser1ToUser2, potentialUser2ToUser1);
    if (count > 0) {
      ArrayList<CardEntity> result1 = new ArrayList<>(
          cardRepository.tradeCardsBetweenUsers(userId1, userId2, count));
      useAllCardAvailable(userId2);
      ArrayList<CardEntity> result2 = new ArrayList<>(
          cardRepository.tradeCardsBetweenUsers(userId2, userId1, count));
      useAllCardAvailable(userId1);
      return Stream.concat(result1.stream(), result2.stream())
          .map(c -> new Card(c.getId(), c.getOwner().getId(),
              c.getAlbum() == null ? Optional.empty() : Optional.of(c.getAlbum().getId()),
              playerMapper.map(c.getPlayer())))
          .toList();
    } else {
      return List.of();
    }
  }

  @Transactional(readOnly = true)
  public Optional<TradingUser> getUserWithCardsAndAlbums(Long userId) {
    Optional<UserEntity> userEntity = userRepository.findByIdWithCardsAndAlbums(userId);
    if (userEntity.isPresent()) {
      UserEntity user = userEntity.get();

      return Optional.of(new TradingUser(new User(user.getId(), user.getUsername()),
          user.getOwnedCards()
              .stream()
              .map(c -> new Card(c.getId(), user.getId(),
                  c.getAlbum() == null ? Optional.empty() : Optional.of(c.getAlbum().getId()),
                  playerMapper.map(c.getPlayer())))
              .toList(),
          user.getOwnedAlbums()
              .stream()
              .map(a -> new Album(a.getId(), a.getTitle(), user.getId()))
              .toList()));
    } else {
      return Optional.empty();
    }
  }

}
