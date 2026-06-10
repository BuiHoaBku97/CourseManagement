package org.example.dao;

import org.example.entity.AdminAccount;

import java.util.Optional;

public interface IAdminAccountDao {
    Optional<AdminAccount> findByUsername(String username);

    AdminAccount getDefaultAdmin();
}
