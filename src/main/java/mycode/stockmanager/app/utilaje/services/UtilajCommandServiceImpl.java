package mycode.stockmanager.app.utilaje.services;


import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.exceptions.NoUtilajFound;
import mycode.stockmanager.app.utilaje.mapper.UtilajMapper;
import mycode.stockmanager.app.utilaje.model.Utilaj;
import mycode.stockmanager.app.utilaje.repository.UtilajeRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UtilajCommandServiceImpl implements UtilajCommandService{

    private UtilajeRepository utilajeRepository;

    @Override
    public UtilajResponseDto createUtilaj(CreateUtilajRequest createUtilajRequest) {
        Utilaj utilaj = Utilaj.builder().name(createUtilajRequest.name()).build();

        utilajeRepository.saveAndFlush(utilaj);

        return UtilajMapper.utilajToResponseDto(utilaj);
    }

    @Override
    public UtilajResponseDto deleteUtilaj(Long id) {
        Utilaj utilaj = utilajeRepository.findUtilajeById(id)
                .orElseThrow(() -> new NoUtilajFound("No utilaj found with this id"));

        UtilajResponseDto utilajResponseDto = UtilajMapper.utilajToResponseDto(utilaj);

        utilajeRepository.delete(utilaj);

        return utilajResponseDto;


    }
}
