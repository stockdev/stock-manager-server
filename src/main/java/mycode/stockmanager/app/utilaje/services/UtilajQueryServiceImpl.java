package mycode.stockmanager.app.utilaje.services;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.dtos.UtilajeResponseList;
import mycode.stockmanager.app.utilaje.exceptions.NoUtilajFound;
import mycode.stockmanager.app.utilaje.mapper.UtilajMapper;
import mycode.stockmanager.app.utilaje.model.Utilaj;
import mycode.stockmanager.app.utilaje.repository.UtilajeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UtilajQueryServiceImpl implements UtilajQueryService{

    private UtilajeRepository utilajeRepository;

    @Override
    public UtilajResponseDto getUtilajByCode(String code) {
        return UtilajMapper.utilajToResponseDto(utilajeRepository.findByCode(code).orElseThrow(() -> new NoUtilajFound("No utilaj found with this code")));
    }


    @Override
    public UtilajeResponseList getAllUtilaje(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Utilaj> utilajPage;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            utilajPage = utilajeRepository.findAll(pageable);
        } else {
            utilajPage = utilajeRepository.findByCodeContainingIgnoreCase(searchTerm, pageable);

            if (utilajPage.isEmpty()) {
                utilajPage = utilajeRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
            }
        }

        if (utilajPage.isEmpty()) {
            throw new NoUtilajFound("No utilaje found");
        }

        return new UtilajeResponseList( UtilajMapper.utilajResponseDtoList(utilajPage.getContent()) , utilajPage.getNumber(), utilajPage.getTotalPages(), utilajPage.getTotalElements());

    }
}
