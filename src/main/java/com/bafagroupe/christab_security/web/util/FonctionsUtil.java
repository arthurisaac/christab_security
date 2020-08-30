package com.bafagroupe.christab_security.web.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class FonctionsUtil implements Serializable {

    /**************** Génération d'un code de 6 chiffres pour ré-initialiser le mot de passe **************/
    public String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public Timestamp getCurrentDateTime() {
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        return ts;
    }
}
