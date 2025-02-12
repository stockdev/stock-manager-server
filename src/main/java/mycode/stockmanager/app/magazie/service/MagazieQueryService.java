package mycode.stockmanager.app.magazie.service;

import mycode.stockmanager.app.magazie.dtos.MagazieResponseList;

public interface MagazieQueryService {


    MagazieResponseList getAllByArticleCode(String articleCode);
}
