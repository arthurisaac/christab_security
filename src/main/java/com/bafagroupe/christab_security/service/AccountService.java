package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.entities.AppUserRoles;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

public interface AccountService {

    // Fonctions de base
    public AppUser saveUser(String email, String password, String passwordConfirmed);
    // public AppUser saveUserVerifMail(String email, String password, String passwordConfirmed);
    public String  confirmUserAccount(String ctoken) throws Exception; // , final RedirectAttributes redirectAttributes) throws Exception;
    // public ModelAndView saveUserVerifMail(ModelAndView m, String email, String password, String passwordConfirmed);
    // public ModelAndView confirmUserAccount(ModelAndView m, String ctoken);
    public AppUser loadUserByEmail(String email);
    public AppRole saveRole(AppRole role);
    public void addRoleToUser(String email, String rolename);
    public boolean recupPassword(String email);
    public boolean verifCode(String email, int code);
    public AppUser reInitPassword(String email, int code, String password, String passwordConfirmed);

    // Ajout de fonctions supplémentaires
    public AppUser activatedUser(AppUser u);
    public AppUser deactivatedUser(AppUser u);
    public AppUser updateUserPassword(AppUser u, String passwordConfirmed, String emailOld, String oldPassword);
    public AppUser updateUser(AppUser u, String emailOld); // , AppRole roles);
    public AppRole updateRole(AppRole r);
    public void deleteUser(AppUser u); // ,  Collection<AppRole> r);
    public void disableUser(AppUser u);
    public void deleteUserRole(AppUser u);
    public void deleteRole(Long id);
    public AppRole loadRole(long id);
    public AppUser loadUser(long id);
    public List<AppRole> loadRoles();
    public List<AppUser> loadUsers();

    // public AppUserRole saveRoleUser(AppUserRole userRole);
    // public AppUserRole updateRoleUser(Long idUser, Long idRole);
    // public void deleteRoleUser(Long idUser);
    // public AppUserRole loadRoleUser(long idR, long idU);
    // public List<AppUserRole> loadRoleUsers();
}
