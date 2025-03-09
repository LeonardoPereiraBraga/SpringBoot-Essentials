package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
    //Validaçoes para nao ser Nulo nem vazio
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
}
