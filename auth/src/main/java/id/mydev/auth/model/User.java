package id.mydev.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usrs", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial", name = "usrs_id")
    private Integer userId;

    @Column(name = "usrs_uuid", length = 64)
    private String userUuid;

    @Column(name = "usrs_mail", length = 100)
    private String userEmail;

    @Column(name = "usrs_usrnme", length = 64)
    private String userUsername;

    @Column(name = "usrs_sttus")
    private Integer userStatus;

    @Column(name = "usrs_pass", length = 150)
    private String userPass;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Jakarta")
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Jakarta")
    private Date updatedDate;

}
