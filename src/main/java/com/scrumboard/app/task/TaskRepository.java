package com.scrumboard.app.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedByUsername(String username);
    List<Task> findByCreatedByUsernameAndStatusIn(String username, List<Status> statusList);
}
