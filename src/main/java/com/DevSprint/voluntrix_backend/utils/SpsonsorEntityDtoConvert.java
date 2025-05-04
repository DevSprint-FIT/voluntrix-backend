package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipPackageDto;

import com.DevSprint.voluntrix_backend.entities.SponsorshipPackageEntity;

import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor


public class SpsonsorEntityDtoConvert {

    private final ModelMapper modelMapper;

    public SponsorshipPackageDto convertToDto(SponsorshipPackageEntity sponsorEntity) {
        return modelMapper.map(sponsorEntity, SponsorshipPackageDto.class);
    }

    public SponsorshipPackageEntity convertToEntity(SponsorshipPackageDto sponsorshipPackageDto) {
        return modelMapper.map(sponsorshipPackageDto, SponsorshipPackageEntity.class);
    }



    public List<SponsorshipPackageDto> convertToDtoList(List<SponsorshipPackageEntity> sponsorshipPackageList) {
        return sponsorshipPackageList.stream().map(entity -> modelMapper.map(entity, SponsorshipPackageDto.class))
                .collect(Collectors.toList());
    
}
}
