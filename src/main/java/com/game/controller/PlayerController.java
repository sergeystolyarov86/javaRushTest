package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.NotValidValueIdException;
import com.game.exceptions.ResourceNotFoundException;
import com.game.repository.specification.PlayersSpecifications;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/rest/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("")
    public List<Player> find(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "race", required = false) Race race,
                             @RequestParam(value = "profession", required = false) Profession profession,
                             @RequestParam(value = "after", required = false) Long after,
                             @RequestParam(value = "before", required = false) Long before,
                             @RequestParam(value = "banned", required = false) Boolean banned,
                             @RequestParam(value = "minExperience", required = false) Integer minExperience,
                             @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                             @RequestParam(value = "minLevel", required = false) Integer minLevel,
                             @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                             @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                             @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
                             @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder playerOrder) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(playerOrder.getFieldName()));

        return playerService.findAll(
                Objects.requireNonNull(Specification.where(PlayersSpecifications.filterByNameLike(name))
                                .and(PlayersSpecifications.filterByTitleLike(title)))
                        .and(PlayersSpecifications.filterEqualsByRace(race))
                        .and(PlayersSpecifications.filterEqualsByProfession(profession))
                        .and(PlayersSpecifications.filterBetweenExperience(minExperience, maxExperience))
                        .and(PlayersSpecifications.filterBetweenLevel(minLevel, maxLevel))
                        .and(PlayersSpecifications.filterBetweenBirthday(after, before))
                        .and(PlayersSpecifications.filterByBanned(banned)),
                pageable).getContent();
    }


    @GetMapping("/{id}")
    public Player findById(@PathVariable("id") Long id) {
        return playerService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found " + id));
    }

    @GetMapping("/count")
    @ResponseBody
    public Long getCount(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "race", required = false) Race race,
                         @RequestParam(value = "profession", required = false) Profession profession,
                         @RequestParam(value = "after", required = false) Long after,
                         @RequestParam(value = "before", required = false) Long before,
                         @RequestParam(value = "banned", required = false) Boolean banned,
                         @RequestParam(value = "minExperience", required = false) Integer minExperience,
                         @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                         @RequestParam(value = "minLevel", required = false) Integer minLevel,
                         @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        return playerService.getCountPlayers(
                Objects.requireNonNull(Specification.where(PlayersSpecifications.filterByNameLike(name))
                                .and(PlayersSpecifications.filterByTitleLike(title)))
                        .and(PlayersSpecifications.filterEqualsByRace(race))
                        .and(PlayersSpecifications.filterEqualsByProfession(profession))
                        .and(PlayersSpecifications.filterBetweenExperience(minExperience, maxExperience))
                        .and(PlayersSpecifications.filterBetweenLevel(minLevel, maxLevel))
                        .and(PlayersSpecifications.filterBetweenBirthday(after, before))
                        .and(PlayersSpecifications.filterByBanned(banned)));
    }

    @PostMapping
    public Player create(@RequestBody Player player) {
        return playerService.create(player);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        playerService.delete(id);
    }


    @PostMapping("{id}")
    public Player update(@PathVariable Long id,@RequestBody  Player updatePlayer ){
        return playerService.edit(id,updatePlayer);
    }
}


