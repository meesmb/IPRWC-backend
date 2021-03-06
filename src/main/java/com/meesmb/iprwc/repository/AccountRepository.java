package com.meesmb.iprwc.repository;

import com.meesmb.iprwc.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);
}
