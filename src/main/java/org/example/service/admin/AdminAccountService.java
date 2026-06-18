package org.example.service.admin;

import org.example.entity.AdminAccount;

public interface AdminAccountService {
    AdminAccount createAdminAccount(String username, String rawPassword);
}
