package com.bafagroupe.christab_security.dao;

import com.bafagroupe.christab_security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    public AppUser findByEmailIgnoreCase(String email);

    @Query("SELECT IFNULL(MAX(idAppUser), 0)+1 AS NEXTID FROM AppUser")
    public long findMaxId();

    @Query("SELECT U FROM AppUser U WHERE U.idAppUser LIKE :y ")
    public AppUser findOneById(@Param("y") long id);

    @Query("DELETE FROM AppUser U WHERE U.idAppUser LIKE :y ")
    public Void deleteOneById(@Param("y") long id);

    @Modifying
    @Query("DELETE FROM AppUserRoles UR" +
            " WHERE UR.appUserByAppUserIdAppUser.email IN(" +
            " SELECT U.email FROM AppUser U WHERE U.email LIKE :y)  ")
    public Void deleteAppUserRoles(@Param("y") String e);
}
