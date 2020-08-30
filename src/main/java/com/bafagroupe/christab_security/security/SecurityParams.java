package com.bafagroupe.christab_security.security;

public class SecurityParams {
    public static final String SECRET = "Vladimir";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final long TIME = 864_000_000; // System.currentTimeMillis()+10*20*3600*1000; // 10 jours * 24 heures * 3600 secondes
    public static final String CLAIM = "Roles";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String EMAIL_FROM = "aurelienbazemo1@gmail.com";
    // public static final String EMAIL_CONTENT = "Pour activer votre compte ChristaB, veuillez cliquer sur ce lien: http://localhost:8087/api/confirmAccount?token=";
    public static final String EMAIL_CONTENT = "Pour activer votre compte ChristaB, veuillez cliquer sur ce lien: http://192.168.1.152:8087/api/confirmAccount?token=";
    public static final String EMAIL_VALIDATE = "Votre compte est en cours de validation!";
    public static final String EMAIL_SUBJECT = "Validation du compte ChristaB";
    public static final String EMAIL_ACTIVATE = "Votre compte est maintenant acitvé ";
    public static final String USERNAME = "r.thur.light@gmail.com"; // "aurelienbazemo1@gmail.com";
    public static final String HOST = "smtp.gmail.com"; // ""ssl0.ovh.net";
    public static final int PORT = 587;
    public static final String PASSWORD = "sourir@rt24"; // "ChristaB"; //mot de passe d'application pour google
    public static String emailRecup ="";
    public static String ACTIVATED_ACCOUNT_MESSAGE="<div>======================== <strong> SUCCES </strong> =======================<br>" +
                                                    "======================================================<br> </div>";
    public static final String MESSAGE = "Bonjour, vous avez demandez à ré-initialiser votre mot de passe? Si oui, veuiller entrer ce code:"
            + " ";
}
