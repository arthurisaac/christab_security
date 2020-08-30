package com.bafagroupe.christab_security.web;

import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.service.AccountServiceI;
import com.bafagroupe.christab_security.web.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final AccountServiceI accountService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register") // @PostMapping("/registerWithMail")
    public AppUser saveUserWMail(@RequestBody UserForm u){
        // return accountService.saveUserVerifMail(u.email, u.password, u.passwordConfirmed);
        return accountService.saveUser(u.email, u.password, u.passwordConfirmed);
    }

    @GetMapping("/confirmAccount")
    public String confirmAccount(@RequestParam("token")String confirmationToken) throws Exception {
        /*System.out.println("*********** Token confirmation ************");
        System.out.println(confirmationToken);
        System.out.println("************************************");*/
        // RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        return accountService.confirmUserAccount(confirmationToken);
    }

    @PostMapping("/users/saveRole")
    public AppRole saveRole(@RequestBody AppRole r) {
        return accountService.saveRole(r);
    }

    @PostMapping("/saveRoleUser")
    public void addRoleToUser(@RequestBody UserCustom u ) {
        accountService.addRoleToUser(u.getEmail(), u.rolename);
    }

    @PostMapping("/users/validateAccount")
    public AppUser validatedAccount(@RequestBody AppUser u) {
        /*AppUser user = new AppUser();
        user.setActivated(true);*/
        return accountService.activatedUser(u);
    }

    @PostMapping("/users/deactivateAccount")
    public AppUser deactivatedAccount(@RequestBody AppUser u) {
        /*AppUser user = new AppUser();
        user.setActivated(true);*/
        return accountService.deactivatedUser(u);
    }

    @PostMapping("/users/updateUserPass")
    public AppUser updateUserPassword(@RequestBody UserR u) {
        AppUser user = new AppUser();
        // user.setIdAppUser(u.idUser);
        user.setEmail(u.email);
        user.setPassword(u.password);
        return accountService.updateUserPassword(user, u.passwordConfirmed, u.emailOld, u.oldPassword);
    }

    @PostMapping("/users/updateUser")
    public AppUser updateUser(@RequestBody UserR u) {
		// System.out.println("*************** Contenu envoyé *********************");
		// System.out.println(u.idUser);
		/*System.out.println(u.email);
		System.out.println(u.password);
		System.out.println(u.passwordConfirmed);
        System.out.println("********** L'ancien email *************");
        System.out.println(u.emailOld);*/
		// System.out.println(u.getRoles());
		// System.out.println("****************************");
        AppUser user = new AppUser();
        // user.setIdAppUser(u.idUser);
        user.setEmail(u.email);
        //
        return accountService.updateUser(user, u.emailOld); // , u.roles);
    }

    @PostMapping("/users/updateRole")
    public AppRole updateRole(@RequestBody AppRole r) {
        return accountService.updateRole(r);
    }


    @PostMapping("/findUser")
    public AppUser loadUser(@RequestBody AppUser u) {
        return accountService.loadUser(u.getIdAppUser());
    }

    @PostMapping("/users/findRole")
    public AppRole loadRole(@RequestBody AppRole r) {
        return accountService.loadRole(r.getIdAppRole());
    }

    @PostMapping("/findUserByEmail")
    public AppUser loadUserByEmail(@RequestBody String email) {
        return accountService.loadUserByEmail(email);
    }

    @GetMapping("/findUsers")
    public List<AppUser> loadUsers() {
        return accountService.loadUsers();
    }

    @GetMapping("/users/findRoles")
    public List<AppRole> loadRoles() {
        return accountService.loadRoles();
    }

    /*@GetMapping("/findGroupeUsers")
    public List<AppUserRole> loadRoleUsers() {
        return accountService.loadRoleUsers();
    }*/

	@PostMapping("/users/deleteUser")
    public void deleteUser(@RequestBody AppUser u) {
		/*System.out.println("********* Utilisateur à supprimer");
		System.out.print(u.getEmail());
		System.out.println(u.getRoles());
		System.out.println("*********");*/
        accountService.deleteUser(u); // , u.getRoles());
    }

    @PostMapping("/users/disableUser")
    public void disableUser(@RequestBody AppUser u) {
        accountService.disableUser(u);
    }

    @PostMapping("/users/deleteRole")
    public void deleteRole(@RequestBody AppRole r) {
		/*System.out.println("********* Role à supprimer");
		System.out.println(r.getIdAppRole());
		System.out.println(r.getRolename());
		System.out.println("*********");*/
        accountService.deleteRole(r.getIdAppRole());
    }

    /*@PostMapping("/deleteRoleUser")
    public void deleteRoleUser(@RequestBody AppUserRole ru) {
        accountService.deleteRoleUser(ru.getAppUserRolePK().getIdAppUser());
    }*/

    @PostMapping("/reInitPassword/{code}")
    public AppUser reInitPassword(@RequestBody UserForm user, @PathVariable int code) {
        /*System.out.println("***********  Avec getters ************ Email: "+user.getEmail()+" Nouveau mot de passe: "+user.getPassword()+" PasswordConfirmé: " +user.getPasswordConfirmed()+" *************");
        System.out.println("*********** Simple ******************* Email: "+user.email+" Nouveau mot de passe : "+user.password+" PasswordConfirmé: " +user.passwordConfirmed+" *************");*/
        return accountService.reInitPassword(user.getEmail(), code, user.getPassword(), user.passwordConfirmed);
        // return accountService.reInitPassword(email, code, user.getPassword(), passwordConfirmed);
    }

    @PostMapping("/recupPassword")
    public boolean recupPassword(@RequestBody String email) {
        return accountService.recupPassword(email);
    }

    @PostMapping("/verifCode/{code}") // /{email}
    public boolean verifCode(@RequestBody String email, @PathVariable int code) {
        return accountService.verifCode(email, code);
    }
}

@Data
class UserForm{
    String email;
    String password;
    String passwordConfirmed;
    // Collection<String> roles;
}

@Data
class UserR{
    long idUser;
    String email;
    String emailOld;
    String password;
    String passwordConfirmed;
    String oldPassword;
}

@Data
class UserCustom {
    String email;
    String rolename;
}
