package models;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name, newName,
            job, newJob;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("last_name")
    private String lastName;
}
