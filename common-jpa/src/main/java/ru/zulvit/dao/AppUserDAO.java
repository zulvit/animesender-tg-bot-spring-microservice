package ru.zulvit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zulvit.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}
