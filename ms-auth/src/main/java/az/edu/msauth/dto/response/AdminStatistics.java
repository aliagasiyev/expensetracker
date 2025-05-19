package az.edu.msauth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatistics {
    private long totalUsers;
    private long activeUsers;
    private long blockedUsers;
    private long newUsersToday;
    private long newUsersThisMonth;
}