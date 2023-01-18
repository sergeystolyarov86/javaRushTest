package com.game.service;

import com.game.entity.Player;

import com.game.exceptions.ResourceNotFoundException;
import com.game.repository.PlayerRepository;
import com.game.validators.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerValidator validator;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerValidator validator) {
        this.playerRepository = playerRepository;
        this.validator = validator;
    }

    public Page<Player> findAll(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }


    public Optional<Player> findById(Long id) {
        validator.validateId(id);
        return playerRepository.findById(id);
    }

    public Long getCountPlayers(Specification<Player> specification) {
        return playerRepository.count(specification);
    }

    private void untilNextLevelValue(Player player) {
        int lvl = (int) (Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100;
        int untilNextLevel = 50 * (lvl + 1) * (lvl + 2) - player.getExperience();
        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLevel);
    }

    public Player create(Player player) {
        validator.validate(player);
        if (player.getBanned() == null)
            player.setBanned(false);
        untilNextLevelValue(player);
        return playerRepository.save(player);
    }

    public void delete(Long id) {
        validator.validateId(id);
        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cant delete player," +
                "player is not exist"));
        validator.validate(player);
        playerRepository.deleteById(id);
    }

    @Transactional
    public Player edit(Long id, Player inputPlayer) {

        validator.validateId(id);

        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cant find player"));

        validator.validateEditPlayer(inputPlayer);

        if (inputPlayer.getName() != null) {
            player.setName(inputPlayer.getName());
        }
        if (inputPlayer.getTitle() != null) {
            player.setTitle(inputPlayer.getTitle());
        }
        if (inputPlayer.getRace() != null) {
            player.setRace(inputPlayer.getRace());
        }
        if (inputPlayer.getProfession() != null) {
            player.setProfession(inputPlayer.getProfession());
        }
        if (inputPlayer.getBirthday() != null) {
            player.setBirthday(inputPlayer.getBirthday());
        }
        if (inputPlayer.getBanned() != null) {
            player.setBanned(inputPlayer.getBanned());
        }
        if (inputPlayer.getExperience() != null) {
            player.setExperience(inputPlayer.getExperience());
            untilNextLevelValue(player);
        }

        return playerRepository.save(player);
    }


}
