package rocks.danielw.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name="users")
public class UserEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable=false)
  private String userId;

  @Column(nullable=false, length=50)
  private String firstName;

  @Column(nullable=false, length=50)
  private String lastName;

  @Column(nullable=false, length=120)
  private String email;

  @Column(nullable=false, length = 60)
  private String encryptedPassword;

  @Column
  private boolean enabled;

  @OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
  private List<AddressEntity> addresses;
	
}
