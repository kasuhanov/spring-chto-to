package su.asgor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "purchases")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="user_seq_gen")
    @SequenceGenerator(name="user_seq_gen", sequenceName="user_seq")
    private long id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Boolean enabled = Boolean.FALSE;
    
    public User() { }

    public User(long id) {
        this.id = id;
    }
    
	public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + "]";
	}
	
}
