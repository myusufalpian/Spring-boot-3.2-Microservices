package id.mydev.auth.repository;

import id.mydev.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true, value = "SELECT a.* FROM public.usrs a where (a.usrs_usrnme = :username or a.usrs_mail = :username) and a.usrs_sttus = :userStatus")
    Optional<User> findUserByUsernameAndUserStatus(String username, Integer userStatus);
}
