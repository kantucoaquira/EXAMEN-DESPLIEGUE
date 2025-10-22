package pe.edu.upeu.sysasistencia.mappers;

import java.util.List;

public interface GenericMapper<D, E> {
    D toDTO(E entity);
    E toEntity(D dto);
    List<D> toDTOs(List<E> entities);
    List<E> toEntities(List<D> dtos);
}