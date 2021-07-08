package me.jongwoo.userservice.repository;

import me.jongwoo.userservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
