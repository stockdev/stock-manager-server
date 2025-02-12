package mycode.stockmanager.app.magazie.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.magazie.dtos.MagazieResponse;
import mycode.stockmanager.app.magazie.dtos.MagazieResponseList;
import mycode.stockmanager.app.magazie.exceptions.MagazieNotFound;
import mycode.stockmanager.app.magazie.mapper.MagazieMapper;
import mycode.stockmanager.app.magazie.model.Magazie;
import mycode.stockmanager.app.magazie.repository.MagazieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MagazieQueryServiceImpl implements  MagazieQueryService{

    private MagazieRepository magazieRepository;

    @Override
    public MagazieResponseList getAllByArticleCode(String articleCode) {
        Optional<List<Magazie>> list = magazieRepository.findAllByArticleCode(articleCode);
        List<MagazieResponse> responses = new ArrayList<>();

        if(list.isEmpty()){
            throw new MagazieNotFound("No magazie found with this article code");
        }else{
            list.get().forEach(magazie -> {
                responses.add(MagazieMapper.magazieToResponseDto(magazie));
            });
        }

        return new MagazieResponseList(responses);
    }
}
