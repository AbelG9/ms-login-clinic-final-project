package com.codigo.mslogin.service;

import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.mslogin.aggregates.request.RequestPersons;

public interface PersonsService {
    ResponseBase getInfoReniec(String numero);
    ResponseBase createPersons(RequestPersons requestPersons);
    ResponseBase findOnePersonById(int id);
    ResponseBase findOnePersonByDocument(String doc);
    ResponseBase findAllPersons();
    ResponseBase updatePerson(int id, RequestPersons requestPersons);
    ResponseBase deletePerson(int id);
}
