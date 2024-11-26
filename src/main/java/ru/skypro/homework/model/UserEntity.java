package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "app_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //id пользователя
    @Column(unique = true, nullable = false)
    private String username; //логин пользователя
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Role role;

    private String phone;
    private String image; //ссылка на аватар

    //1
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdEntity> adsList;

    //2
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentsList;

    /**
     * В данной связи с ImageEntity есть загвоздка с Spring Security
     * насколько я понимаю на текущий момент, при автозагрузке пользователя
     * спринг загружает и связный объект, но он ругается на размер
     * якобы загружем слишком много данных и надо бы оптимизировать
     * из-за этого нужно подключить (fetch = FetchType.LAZY)
     * без него перестаёт работать адекватно авторизация,
     * но пока я не проверил, что будет происходить если мы обратимся
     * к данному связному объекту и он всё-таки загрузится
     */
    @OneToOne(fetch = FetchType.LAZY) //Spring Security не очень хочет, чтобы загрузка связных объектов была сразу
    @JoinColumn(name = "user_image_id", referencedColumnName = "imageId")
    private ImageEntity imageEntity;


}
