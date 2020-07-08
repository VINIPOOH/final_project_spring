package ua.testing.delivery.dto;

import lombok.EqualsAndHashCode;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@EqualsAndHashCode
public class LocaliseLocalityDto {
    private Long id;
    private String name;

    public LocaliseLocalityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public static LocaliseLocalityDtoBuilder builder() {
        return new LocaliseLocalityDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static class LocaliseLocalityDtoBuilder {
        private Long id;
        private String name;

        LocaliseLocalityDtoBuilder() {
        }

        public LocaliseLocalityDto.LocaliseLocalityDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocaliseLocalityDto.LocaliseLocalityDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LocaliseLocalityDto build() {
            return new LocaliseLocalityDto(id, name);
        }


    }
}
