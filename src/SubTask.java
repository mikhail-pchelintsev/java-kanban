public class SubTask extends Task {
    private Long subTaskId;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Long getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(Long subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "subTaskId=" + subTaskId +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}