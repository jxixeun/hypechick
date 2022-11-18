package hongik.graduation.hypechick.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByMember_Id(Long memberId);

    @Query("SELECT t FROM Tasks t ORDER BY t.id DESC")
    List <Tasks> findAllTasks();
}

