package hongik.graduation.hypechick.task;

import hongik.graduation.hypechick.member.Member;
import hongik.graduation.hypechick.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    /**
     * 투두 생성과 저장
     */
    @Transactional
    public Long save(Long memberId, String contents, LocalDate date) {
        Member member = memberRepository.findById(memberId).get();
        Tasks task = Tasks.builder()
                .member(member)
                .contents(contents)
                .date(date)
                .build();
        return taskRepository.save(task).getId();
    }

    /**
     * 투두 수정
     */
    @Transactional
    public Long update(Long taskId, String contents) {
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + taskId));
        task.update(contents);
        return taskId;
    }

    /**
     * 투두 아이디로 조회
     */
    @Transactional(readOnly = true)
    public Tasks findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + taskId));
    }

    /**
     * 투두 삭제
     */
    @Transactional
    public void delete(Long taskId) {
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + taskId));
        taskRepository.delete(task);
    }

    /**
     * 회원이 쓴 전체 투두리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Tasks> findMemberTasks(Long memberId) {
        return taskRepository.findByMember_Id(memberId);
    }

    /**
     * 투두 체크
     */
    @Transactional
    public void check(Long taskId){
        Tasks task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투두가 없습니다. id=" + taskId));
        task.check();
    }

    /**
     * 전체 투두리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Tasks> findAllTasks() {
        return taskRepository.findAllTasks();
    }

}

