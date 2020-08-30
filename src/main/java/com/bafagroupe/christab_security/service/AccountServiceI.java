package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.dao.AppRoleRepository;
import com.bafagroupe.christab_security.dao.AppUserRepository;
import com.bafagroupe.christab_security.dao.ConfirmationTokenRepository;
import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.entities.ConfirmationToken;
import com.bafagroupe.christab_security.security.SecurityParams;
import com.bafagroupe.christab_security.web.util.FonctionsUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;

@Transactional // signifie qu'à chaque fois qu'une méthode est utilisée à la fin il fait un commit automatiquement
@AllArgsConstructor
@Service
public class AccountServiceI implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    // private AppUserRolesRepository appUserRolesRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenRepository confirmationTokenRepository;
    // private EmailSenderService emailSenderService;


    /****************** ==== Enregistrement ==== ***************/
    @Override
    public
    AppUser saveUser(String email, String password, String passwordConfirmed) {
        /*System.out.print("************ Email ********");
        System.out.print(email);
        System.out.print("************ Password ********");
        System.out.print(password);
        System.out.print("************ confirmer Password ********");
        System.out.print(passwordConfirmed);
        System.out.print("********************");*/
        AppUser user = appUserRepository.findByEmailIgnoreCase(email);

        if(user != null) throw new RuntimeException("User already exists");
        if(!password.equals(passwordConfirmed)) throw new RuntimeException("Please confirm password");
        AppUser appUser = new AppUser();
        // appUser.setIdAppUser(appUserRepository.findMaxId());
        appUser.setEmail(email);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        // appUser.setActivated(true);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mailMessage.setSubject(SecurityParams.EMAIL_SUBJECT);
            mailMessage.setText(SecurityParams.EMAIL_VALIDATE);

            Transport.send(mailMessage);
            System.out.println("Mail de validation envoyé!");


            appUserRepository.save(appUser);
            addRoleToUser(email, "USER");


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return appUser;
    }

    /****************** ==== Fonction supplémentaire (Activation du compte d'un utilisateur) ==== ***************/
    @Override
    public AppUser activatedUser(AppUser u) {
        AppUser updUser = appUserRepository.findByEmailIgnoreCase(u.getEmail());
        if(updUser != null) {
            System.out.println(updUser.getEmail());
            updUser.setValidated(true);
        }


        ConfirmationToken confirmationToken = new ConfirmationToken(updUser);
        // confirmationToken.setTokenid(confirmationTokenRepository.findMaxId());
        confirmationTokenRepository.save(confirmationToken);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(updUser.getEmail()));
            mailMessage.setSubject("Activation du compte ChristaB!");
            mailMessage.setText(SecurityParams.EMAIL_CONTENT+confirmationToken.getConfirmationToken());

            Transport.send(mailMessage);
            System.out.println("Mail de confirmation de l'email envoyé");

            appUserRepository.save(updUser);
            System.out.println("Mail d'activation envoyé!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return updUser;
    }

    /****************** ==== Enregistrement avec vérification de l'email ==== ***************/
    /*@Override
    public AppUser saveUserVerifMail(String email, String password, String passwordConfirmed) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(email);

        if(user != null) throw new RuntimeException("User already exists");
        if(!password.equals(passwordConfirmed)) throw new RuntimeException("Please confirm password");
        AppUser appUser = new AppUser();
        appUser.setIdAppUser(appUserRepository.findMaxId());
        appUser.setEmail(email);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        // appUser.setActivated(true);

        ConfirmationToken confirmationToken = new ConfirmationToken(appUser);
        confirmationToken.setTokenid(confirmationTokenRepository.findMaxId());
        confirmationTokenRepository.save(confirmationToken);

        Properties properties = new Properties();
        // properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.EMAIL_FROM));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(appUser.getEmail()));
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText(SecurityParams.EMAIL_CONTENT+confirmationToken.getConfirmationToken());

            Transport.send(mailMessage);
            System.out.println("Mail de confirmation envoyé");


        appUserRepository.save(appUser);
        // System.out.println("*********** L'enregistrement de l'utilisateur: "+appUser);
        addRoleToUser(email, "USER");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return appUser;
    }*/

    /****************** ==== Confirmation du compte ==== ***************/
    @Override
    public String confirmUserAccount(String confirmationToken) throws Exception {

        final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token == null) throw new Exception("Erreur: Le lien est invalide ou interrompu!");
        AppUser user = appUserRepository.findByEmailIgnoreCase(token.getUser().getEmail());
        user.setActivated(true);
        appUserRepository.save(user);
        System.out.println("Compte vérifié avec succès");
        redirectAttributes.addFlashAttribute("Vérification du compte", user.getEmail()+" résussi");

        return SecurityParams.ACTIVATED_ACCOUNT_MESSAGE+"Compte de "+user.getEmail()+" vérifié avec succès!";
    }

    /****************** ==== Affichage d'un utilisateur  ==== ***************/
    @Override
    public AppUser loadUserByEmail(String email) {
        return appUserRepository.findByEmailIgnoreCase(email);
    }

    /****************** ==== Enregistrement d'un droit  ==== ***************/
    @Override
    public AppRole saveRole(AppRole role) {
        // role.setIdAppRole(appRoleRepository.findMaxId());
        return appRoleRepository.save(role);
    }

    /****************** ==== Attribution d'un droit à un utilisateur  ==== ***************/
    @Override
    public void addRoleToUser(String email, String rolename) {
        // System.out.println("*********** Recherche de l'utilisateur par son email: "+email);
        AppUser appUser = appUserRepository.findByEmailIgnoreCase(email);
        // System.out.println("*********** Recherche du rôle ar son nom: "+rolename);
        AppRole appRole = appRoleRepository.findByRolename(rolename);
        appUser.getRoles().add(appRole);
    }

/*********************************** Ré-initialisation du mot de passe d'un utilisateur ****************************/
    @Override
    public boolean recupPassword(String email) {
        SecurityParams.emailRecup = email;
        int code;

        FonctionsUtil fu = new FonctionsUtil();
        code = Integer.parseInt(fu.getRandomNumberString());
        AppUser user = appUserRepository.findByEmailIgnoreCase(email);

		System.out.println(email);
		System.out.println(user);

        // permet de vérifier si l'email existe vraiment
        if(user.getEmail().equals(email)) {


            // final String userFrom = "kafarsi@ardive.net";
            // final String host = "ssl0.ovh.net";
            // final int port = 587; //le port par défaut du serveur smtp ovh

            Properties prop = new Properties();
            prop.put("mail.smtp.host", SecurityParams.HOST);
            prop.put("mail.smtp.port",SecurityParams.PORT);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true"); //TLS

            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD); // SecurityParams.PASS);
                }
            });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SecurityParams.USERNAME));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(email)
                );
                message.setSubject("Code de ré-initialisation du mot de passe");
                message.setText(SecurityParams.MESSAGE +code );

                Transport.send(message);
                System.out.println("Code envoyé dans le mail");

                user.setPasswordForget(code);
                user.setLimitPasswordTime(fu.getCurrentDateTime());
                appUserRepository.save(user);
                // System.out.println(code);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            // verifCode(email, code);
            // System.out.println("Email valide");
            return true;
        }
        else {
            // System.out.println("Email non valide");
            return false;
        }
    }

    /****************** ==== Vérification du code envoyé ==== ***************/
    @Override
    public boolean verifCode(String email, int code) {
        FonctionsUtil fu = new FonctionsUtil();

        AppUser user = appUserRepository.findByEmailIgnoreCase(email);
		/*System.out.println(code);
		System.out.println(email);*/

        if( (code == user.getPasswordForget()) && (fu.getCurrentDateTime().compareTo(user.getLimitPasswordTime()) < 30*60*1000 ) ) {
            // System.out.println("code valide");
            return true;
        }
        else {
            // System.out.println("code invalide");
            return false;
        }
    }

    /****************** ==== Ré-initialisation du mot de passe ==== ***************/
    @Override
    public AppUser reInitPassword(String email, int code, String password, String passwordConfirmed) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(email);

        // permet de vérifier si l'email existe vraiment
        if(user.getEmail().equals(email) && verifCode(email, code)) {
            if(!password.equals(passwordConfirmed)) throw new RuntimeException("Please confirm password");
            user.setPassword(bCryptPasswordEncoder.encode(password));
            appUserRepository.save(user);
            // System.out.println("*********** L'enregistrement du mot de passe de l'utilisateur: "+user);
            // return user;
        }
        return user;
    }

/*************************************************** Fonctions supplémentaire ********************************************/


    /****************** ==== Activation du compte d'un utilisateur ==== ***************/
    /*@Override
    public AppUser activatedUser(AppUser u) {
        AppUser updUser = appUserRepository.findByEmailIgnoreCase(u.getEmail());
        if(updUser != null) {
            System.out.println(updUser.getEmail());
            updUser.setValidated(true);
        }


        ConfirmationToken confirmationToken = new ConfirmationToken(updUser);
        confirmationToken.setTokenid(confirmationTokenRepository.findMaxId());
        confirmationTokenRepository.save(confirmationToken);

        Properties properties = new Properties();
        // properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(updUser.getEmail()));
            mailMessage.setSubject("Activation du compte!");
            mailMessage.setText(SecurityParams.EMAIL_CONTENT+confirmationToken.getConfirmationToken());

            Transport.send(mailMessage);
            System.out.println("Mail de confirmation de l'email envoyé");


            appUserRepository.save(updUser);
            System.out.println("Mail d'activation envoyé!");
            addRoleToUser(updUser.getEmail(), "USER");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        *//*Properties properties = new Properties();
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getEmail()));
            mailMessage.setSubject("Activation du compte!");
            mailMessage.setText(SecurityParams.EMAIL_ACTIVATE);

            Transport.send(mailMessage);
            System.out.println("Mail d'activation envoyé!");


            appUserRepository.save(updUser);

        } catch (MessagingException e) {
            e.printStackTrace();
        }*//*

        return updUser;
    }*/

    /****************** ==== Désactivation du compte d'un utilisateur ==== ***************/
    @Override
    public AppUser deactivatedUser(AppUser u) {
        AppUser updUser = appUserRepository.findByEmailIgnoreCase(u.getEmail());
        updUser.setActivated(false);
        appUserRepository.save(updUser);

        return updUser;
    }
    /*=============*/
    @Override
    public void disableUser(AppUser u) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(u.getEmail());
        if(user != null) {
            user.setActivated(false);
        }
    }

    /****************** ==== Modification du mot de passe d'un utilisateur ==== ***************/
    @Override
    public AppUser updateUserPassword(AppUser u, String passwordConfirmed, String emailOld, String oldPassword) {
        /*System.out.println("************* Recherche **************");
        System.out.println(u.getEmail());
        System.out.println("************* Fin recherche **************");
        System.out.println("************* Email **************");
        System.out.println(u.getEmail());*/
        AppUser updUser = appUserRepository.findByEmailIgnoreCase(emailOld);

        if(updUser != null) {
            /*System.out.println("*********** Utilisateur à modifier Service ***********");
            if(bCryptPasswordEncoder.matches(oldPassword, updUser.getPassword())) {
                System.out.println("== Correspondance parfaite ==");
            }
            else {
                System.out.println("== Les mots de passe ne correspondent pas ==");
            }
            System.out.println("**********************");*/

        if(!bCryptPasswordEncoder.matches(oldPassword, updUser.getPassword())) throw new RuntimeException("Ancien mot de passe incorrecte");
        if(!u.getPassword().equals(passwordConfirmed)) throw new RuntimeException("Les mots de passe ne correspondent pas");
        updUser.setEmail(u.getEmail());
        updUser.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
        appUserRepository.save(updUser);
            // updateRoleUser(u.getIdAppUser(), roles.getIdAppRole());
        }

        return updUser;
    }

    /****************** ==== Modification du compte  d'un utilisateur ==== ***************/
    @Override
    public AppUser updateUser(AppUser u, String emailOld) {
        AppUser updUser = appUserRepository.findByEmailIgnoreCase(emailOld);
        updUser.setEmail(u.getEmail());
        appUserRepository.save(updUser);

        return updUser;
    }

    /****************** ==== Activation du droit d'un utilisateur ==== ***************/
    @Override
    public AppRole updateRole(AppRole r) {
        AppRole updR = appRoleRepository.findOneById(r.getIdAppRole());
        updR.setRolename(r.getRolename());
        appRoleRepository.save(updR);
        return updR;
    }

    /****************** ==== Suppression du compte d'un utilisateur ==== ***************/
    @Override
    public void deleteUser(AppUser u) { //, Collection<AppRole> r) {
        deleteUserRole(u);
        appUserRepository.delete(u);
    }


    @org.springframework.transaction.annotation.Transactional
    @Override
    public void deleteUserRole(AppUser u) {
        appUserRepository.deleteAppUserRoles(u.getEmail());
    }

    @Override
    public void deleteRole(Long id) {
        appRoleRepository.deleteOneById(id);
    }

    @Override
    public AppRole loadRole(long id) {
        return appRoleRepository.findOneById(id);
    }

    @Override
    public AppUser loadUser(long id) {
        return appUserRepository.findOneById(id);
    }


    @Override
    public List<AppRole> loadRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public List<AppUser> loadUsers() {
        return appUserRepository.findAll();
    }

}
