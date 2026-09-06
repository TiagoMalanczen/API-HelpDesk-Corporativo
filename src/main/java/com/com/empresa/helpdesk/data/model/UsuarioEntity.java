package com.com.empresa.helpdesk.data.model;


import com.com.empresa.helpdesk.data.enums.PerfilUsuarioEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
@EqualsAndHashCode(of = "id")
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column
    @Builder.Default
    private boolean ativo = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilUsuarioEnum perfil;

}
