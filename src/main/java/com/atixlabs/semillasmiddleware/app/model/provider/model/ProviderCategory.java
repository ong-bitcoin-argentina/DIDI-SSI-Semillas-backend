package com.atixlabs.semillasmiddleware.app.model.provider.model;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table
public class ProviderCategory {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Unique
    private String name;

    public ProviderCategory(String name){
        this.name = name;
    }

    public ProviderCategoryDto toDto(){
        return ProviderCategoryDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
