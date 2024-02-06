package com.codigo.mslogin.service.impl;

import com.codigo.appointmentslibrary.constants.Constants;
import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.appointmentslibrary.util.Util;
import com.codigo.mslogin.aggregates.request.RequestPersons;
import com.codigo.mslogin.aggregates.response.ResponseReniec;
import com.codigo.mslogin.entity.DocumentsTypeEntity;
import com.codigo.mslogin.entity.PersonsEntity;
import com.codigo.mslogin.feignclient.ReniecClient;
import com.codigo.mslogin.repository.DocumentsTypeRepository;
import com.codigo.mslogin.repository.PersonsRepository;
import com.codigo.mslogin.service.PersonsService;
import com.codigo.mslogin.util.PersonsValidations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonsServiceImpl implements PersonsService {
    private final ReniecClient reniecClient;
    private final PersonsRepository personsRepository;
    private final PersonsValidations personsValidations;
    private final DocumentsTypeRepository documentsTypeRepository;

    public PersonsServiceImpl(ReniecClient reniecClient, PersonsRepository personsRepository, PersonsValidations personsValidations, DocumentsTypeRepository documentsTypeRepository) {
        this.reniecClient = reniecClient;
        this.personsRepository = personsRepository;
        this.personsValidations = personsValidations;
        this.documentsTypeRepository = documentsTypeRepository;
    }

    @Value("${token.api.reniec}")
    private String tokenReniec;

    @Value("${time.expiration.reniec.info}")
    private String timeExpirationReniecInfo;

    @Override
    public ResponseBase getInfoReniec(String numero) {
        ResponseReniec reniec = getExecutionReniec(numero);
        if (reniec != null) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(reniec));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_NON_DATA_RENIEC, Optional.empty());
        }
    }

    private ResponseReniec getExecutionReniec(String numero) {
        String authorization = "Bearer "+tokenReniec;
        return reniecClient.getInfoReniec(numero, authorization);
    }

    @Override
    public ResponseBase createPersons(RequestPersons requestPersons) {
        boolean validatePersons = personsValidations.validateInput(requestPersons, false);
        if (validatePersons) {
            PersonsEntity personsEntity = getPersonsEntity(requestPersons);
            personsRepository.save(personsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOnePersonById(int id) {
        Optional<PersonsEntity> person = personsRepository.findById(id);
        if (person.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, person);
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOnePersonByDocument(String doc) {
        Optional<PersonsEntity> person = personsRepository.findByNumDocument(doc);
        if (person.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, person);
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findAllPersons() {
        List<PersonsEntity> persons = personsRepository.findAll();
        if (!persons.isEmpty()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(persons));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase updatePerson(int id, RequestPersons requestPersons) {
        boolean existsPerson = personsRepository.existsById(id);
        if (existsPerson) {
            Optional<PersonsEntity> persons = personsRepository.findById(id);
            boolean validationEntity = personsValidations.validateInput(requestPersons, true);
            if (validationEntity) {
                PersonsEntity personsUpdate = getPersonsEntityUpdate(requestPersons, persons.get());
                personsRepository.save(personsUpdate);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsUpdate));
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
            }
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ERROR_NOT_UPDATE_PERSONS, Optional.empty());
        }
    }

    @Override
    public ResponseBase deletePerson(int id) {
        boolean existsPerson = personsRepository.existsById(id);
        if (existsPerson) {
            Optional<PersonsEntity> persons = personsRepository.findById(id);
            PersonsEntity personsEntity = getPersonsEntityDeleted(persons.get());
            personsRepository.save(personsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_EXIST, Constants.MESSAGE_ERROR_NOT_DELETE_PERSONS, Optional.empty());
        }
    }

    private PersonsEntity getPersonsEntity(RequestPersons requestPersons) {
        PersonsEntity personsEntity = new PersonsEntity();
        return getPersons(requestPersons, personsEntity, false);
    }

    private PersonsEntity getPersonsEntityUpdate(RequestPersons requestPersons, PersonsEntity personsEntity) {
        return getPersons(requestPersons, personsEntity, true);
    }

    private PersonsEntity getPersons(RequestPersons requestPersons, PersonsEntity personsEntity, boolean isUpdate) {
        ResponseReniec reniec = getExecutionReniec(requestPersons.getNumDocument());
        if (reniec != null) {
            personsEntity.setName(reniec.getNombres());
            personsEntity.setLastname(reniec.getApellidoPaterno() + " " + reniec.getApellidoMaterno());
            personsEntity.setNumDocument(reniec.getNumeroDocumento());
        } else {
            return null;
        }

        personsEntity.setEmail(requestPersons.getEmail());
        personsEntity.setPhoneNumber(requestPersons.getPhoneNumber());
        personsEntity.setGender(requestPersons.getGender());
        personsEntity.setStatus(Constants.STATUS_ACTIVE);
        personsEntity.setDocumentsTypeEntity(getDocumentsTypeEntity(requestPersons));

        if (isUpdate) {
            personsEntity.setUserModified(Constants.AUDIT_ADMIN);
            personsEntity.setDateModified(Util.getActualTimestamp());
        } else {
            personsEntity.setUserCreated(Constants.AUDIT_ADMIN);
            personsEntity.setDateCreated(Util.getActualTimestamp());
        }
        return personsEntity;
    }

    private PersonsEntity getPersonsEntityDeleted(PersonsEntity personsEntity) {
        personsEntity.setStatus(Constants.STATUS_INACTIVE);
        personsEntity.setDateDeleted(Util.getActualTimestamp());
        personsEntity.setUserDeleted(Constants.AUDIT_ADMIN);
        return personsEntity;
    }

    private DocumentsTypeEntity getDocumentsTypeEntity(RequestPersons requestPersons) {
        return documentsTypeRepository.findById(requestPersons.getDocumentTypeId()).orElse(null);
    }
}
