package com.game.validators;


import com.game.entity.Player;
import com.game.exceptions.NotValidValueIdException;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

@Component
public class PlayerValidator {
    private static final int MAX_LENGTH_NAME = 12;
    private static final int MAX_LENGTH_TITLE = 30;
    private static final int MAX_SIZE_EXPERIENCE = 10000000;
    private static final Long MIN_BIRTHDAY = new GregorianCalendar(2000, 0, 0).getTime().getTime();
    private static final Long MAX_BIRTHDAY = new GregorianCalendar(3000, 0, 0).getTime().getTime();


    public void validate(Player player) {
        if (player.getName() == null || player.getName().length() >= MAX_LENGTH_NAME || player.getName().isEmpty())
            throw new NotValidValueIdException("Name length must be less 13 characters");

        if (player.getTitle().length() >= MAX_LENGTH_TITLE)
            throw new NotValidValueIdException("Title length must be less 30 characters");

        if (player.getRace() == null) throw new NotValidValueIdException("Race cant be null");

        if (player.getProfession() == null) throw new NotValidValueIdException("Profession cant be null");

        if (player.getBirthday() == null || player.getBirthday().getTime() < MIN_BIRTHDAY ||
                player.getBirthday().getTime() > MAX_BIRTHDAY)
            throw new NotValidValueIdException("Incorrect date value");

        if (player.getExperience() < 0 || player.getExperience() >= MAX_SIZE_EXPERIENCE)
            throw new NotValidValueIdException("Incorrect experience value");

    }

    public void validateId(Long id) {
        if (id < 1) throw new NotValidValueIdException("Id cant be <=0");
    }

    public void validateEditPlayer(Player player) {


        if (player.getName() != null) {
            if (player.getName().length() >= MAX_LENGTH_NAME || player.getName().isEmpty())
                throw new NotValidValueIdException("Not valid name");
        }

        if (player.getTitle() != null) {
            if (player.getTitle().length() >= MAX_LENGTH_TITLE)
                throw new NotValidValueIdException("Not valid title");
        }

        if (player.getBirthday() != null) {
            if (player.getBirthday().getTime() < MIN_BIRTHDAY ||
                    player.getBirthday().getTime() > MAX_BIRTHDAY)
                throw new NotValidValueIdException("Not valid birthday");
        }

        if (player.getExperience() != null) {
            if (player.getExperience() < 0 || player.getExperience() >= MAX_SIZE_EXPERIENCE)
                throw new NotValidValueIdException("Not valid experience");
        }
    }


}
