package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private Long subTaskId;
    private Duration duration;
    private LocalDateTime startTime;
    private static final DateTimeFormatter FORMATTER_FOR_YEAR = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public SubTask(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, status);
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getEndTime() {
        return startTime.plus(duration).format(FORMATTER_FOR_YEAR);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);  // формат ЧЧ:ММ
    }

    public Long getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(Long subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String toString() {
        return "model.SubTask{" +
                "subTaskId=" + subTaskId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + formatDuration(duration) +
                ", startTime=" + startTime.format(FORMATTER_FOR_YEAR) +
                ", endTime=" + getEndTime() +
                '}';
    }
}