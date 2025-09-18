package devdan.restful.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponse {

    private String id;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;
}
