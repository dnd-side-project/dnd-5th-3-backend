package com.dnd5th3.dnd5th3backend.repository.notice;

import com.dnd5th3.dnd5th3backend.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long>,NoticeRepositoryCustom {

}
