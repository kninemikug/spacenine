package miniProject;

import java.util.Date;
import lombok.Data;

@Data
public class Logs {
    private int no;
    private String user_id;
    private String event;
    private Date time;
}
