package ru.zulvit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zulvit.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
