package hongik.graduation.hypechick.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TaskApiController {
    @Autowired
    TaskService taskService;

    //투두 조회
    @GetMapping("/api/tasks/{memberId}/{taskId}")
    public TaskDto findById(@PathVariable Long memberId, @PathVariable Long taskId) {
        Tasks task = taskService.findById(taskId);
        return new TaskDto(task.getId(), task.getContents(), task.getIsChecked(), task.getDate());
    }

    //유저가 쓴 전체 투두 조회
    @GetMapping("/api/tasks/{memberId}")
    public Result findUserTask(@PathVariable Long memberId) {
        List<Tasks> memberTasks = taskService.findMemberTasks(memberId);
        List<TaskDto> collect = memberTasks.stream()
                .map(t -> new TaskDto((t.getId()), t.getContents(), t.getIsChecked(), t.getDate()))
                .collect(Collectors.toList());
        return new Result<>(collect);
    }

    /**
     * 투두 등록
     */

    @PostMapping("/api/tasks/{memberId}")
    public CreateTaskResponse save(@PathVariable Long memberId, @RequestBody @Valid CreateTaskRequest request) {
        Long taskId = taskService.save(memberId, request.getContents(), request.getDate());
        return new CreateTaskResponse(taskId, request.getContents(), request.getDate());
    }

    /**
     * 투두 수정
     */
    @PutMapping("/api/tasks/{memberId}/{taskId}")
    public UpdateTaskResponse update(@PathVariable Long taskId, @RequestBody @Valid UpdateTaskRequest request) {
        Long id = taskService.update(taskId, request.getContents());
        return new UpdateTaskResponse(id, request.getContents());
    }

    //투두 삭제 (예외처리 꼭 추가필요)
    @DeleteMapping("/api/tasks/{userId}/{taskId}")
    public Long deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return taskId;
    }

    @GetMapping("/api/tasks/check/{taskId}")
    public Boolean checkTask(@PathVariable Long taskId) {
        taskService.check(taskId);
        return taskService.findById(taskId).getIsChecked();
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class TaskDto {
        private Long taskId;
        private String contents;
        private Boolean isChecked;
        private LocalDate date;
    }

    @Data
    static class CreateTaskRequest {
        @NotEmpty
        private String contents;
        private LocalDate date;
    }

    @Data
    static class CreateTaskResponse {
        private Long taskId;
        private String contents;
        private Boolean isChecked;
        private LocalDate date;

        public CreateTaskResponse(Long taskId, String contents, LocalDate date) {
            this.taskId = taskId;
            this.contents = contents;
            this.date = date;
            this.isChecked = false;
        }
    }

    @Data
    static class UpdateTaskRequest {
        @NotEmpty
        private String contents;
    }

    @Data
    @AllArgsConstructor
    static class UpdateTaskResponse {
        private Long taskId;
        private String contents;
    }

}
