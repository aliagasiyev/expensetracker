package az.edu.msanalytics.client;

import az.edu.msanalytics.model.dto.request.ExpenseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ExpenseClient {

    private final RestTemplate restTemplate;

    public ExpenseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExpenseDto> getExpensesByUser(Long userId, int year) {
        String url = "http://ms-expense/api/v1/expenses?userId=" + userId + "&year=" + year;
        ExpenseDto[] response = restTemplate.getForObject(url, ExpenseDto[].class);
        assert response != null;
        return Arrays.asList(response);
    }
}
