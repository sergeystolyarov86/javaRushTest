package com.game.repository.specification;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;


import java.util.Date;

public class PlayersSpecifications {

    public static Specification<Player> filterByNameLike(String namePart) {
        return (root, criteriaQuery, criteriaBuilder) -> namePart == null ? null : criteriaBuilder.like(root.get("name"), String.format("%%%s%%", namePart));
    }

    public static Specification<Player> filterByTitleLike(String title) {
        return (root, criteriaQuery, criteriaBuilder) -> title == null ? null : criteriaBuilder.like(root.get("title"), String.format("%%%s%%", title));
    }

    public static Specification<Player> filterEqualsByRace(Race race) {
        return (root, criteriaQuery, criteriaBuilder) -> race == null ? null : criteriaBuilder.equal(root.get("race"), race);
    }


    public static Specification<Player> filterEqualsByProfession(Profession profession) {
        return (root, criteriaQuery, criteriaBuilder) -> profession == null ? null : criteriaBuilder.equal(root.get("profession"), profession);
    }

    public static Specification<Player> filterBetweenBirthday(Long after, Long before) {
        return ((root, query, criteriaBuilder) -> {
            if (after == null && before == null) {
                return null;
            } else if (after == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            } else if (before == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            } else {
                return criteriaBuilder.between(root.get("birthday"), new Date(after), new Date(before));
            }
        });
    }


    public static Specification<Player> filterByBanned(Boolean isBanned) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (isBanned == null) return null;
            if (isBanned) return criteriaBuilder.isTrue(root.get("banned"));
            return criteriaBuilder.isFalse(root.get("banned"));
        };
    }


    public static Specification<Player> filterBetweenExperience(Integer minExperience, Integer maxExperience) {
        return ((root, query, criteriaBuilder) -> {
            if (minExperience == null && maxExperience == null) {
                return null;
            } else if (minExperience == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience);
            } else if (maxExperience == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience);
            } else {
                return criteriaBuilder.between(root.get("experience"), minExperience, maxExperience);
            }
        });
    }

    public static Specification<Player> filterBetweenLevel(Integer min, Integer max) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (min == null && max == null) return null;
            if (min == null) return criteriaBuilder.lessThanOrEqualTo(root.get("level"), max);
            if (max == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("level"), min);
            return criteriaBuilder.between(root.get("level"), min, max);
        };
    }
}
