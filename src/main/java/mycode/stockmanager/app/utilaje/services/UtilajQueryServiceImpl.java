package mycode.stockmanager.app.utilaje.services;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.exceptions.NoUtilajFound;
import mycode.stockmanager.app.utilaje.mapper.UtilajMapper;
import mycode.stockmanager.app.utilaje.model.Utilaj;
import mycode.stockmanager.app.utilaje.repository.UtilajeRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UtilajQueryServiceImpl implements UtilajQueryService{

    private UtilajeRepository utilajeRepository;

    @Override
    public UtilajResponseDto getUtilajById(Long Id) {
        return UtilajMapper.utilajToResponseDto(utilajeRepository.findUtilajeById(Id).orElseThrow(() -> new NoUtilajFound("No utilaj found with this id")));
    }
}
