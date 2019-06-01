package rocks.danielw.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity(name="addresses")
public class AddressEntity implements Serializable {

  public static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;
	
  @Column(length=30, nullable=false)
  private String addressId;

  @Column(length=15, nullable=false)
  private String city;

  @Column(length=30, nullable=false)
  private String country;

  @Column(length=100, nullable=false)
  private String streetName;

  @Column(length=5, nullable=false)
  private String postalCode;

  @Column(length=10, nullable=false)
  private String type;

  @ManyToOne
  @JoinColumn(name="users_id")
  private UserEntity userDetails;

}
