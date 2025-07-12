package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventDTOConverter {

    // Event Mapping
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    // EventEntity to EventDTO
    public EventDTO toEventDTO(EventEntity eventEntity) {
        return modelMapper.map(eventEntity, EventDTO.class);
    }

    // EventDTO to EventEntity
    public EventEntity toEventEntity(EventDTO eventDTO) {
        EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);
        if (eventDTO.getCategories() != null) {
            Set<CategoryEntity> categoryEntities = eventDTO.getCategories().stream()
                    .map(dto -> categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + dto.getCategoryId())))
                    .collect(Collectors.toSet());
            eventEntity.setCategories(categoryEntities);
        }
        return eventEntity;
    }

    // List<EventEntity> to List<EventDTO>
    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntityList) {
        return eventEntityList.stream().map(entity -> modelMapper.map(entity, EventDTO.class))
                .collect(Collectors.toList());
    }
}

