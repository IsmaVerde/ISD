namespace java es.udc.ws.runfic.thrift

struct ThriftRaceDto {
    1: i64 raceID;
    2: string city;
    3: string description;
    4: string startDateTime;
    5: double price;
    6: i32 participants;
    7: i32 maxParticipants;
}

struct ThriftInscriptionDto{
    1: i64 inscriptionID;
    2: string user;
    3: string creditCardNumber;
    4: i64 raceID;
    5: i32 runnerNumber;
    6: bool isNumberTaken;
}

exception ThriftInputValidationException {
    1: string message;
}

exception ThriftInstanceNotFoundException {
    1: string instanceId;
    2: string instanceType;
}

exception ThriftAlreadyInscribedException {
    1: string message;
}

exception ThriftInscriptionClosedException {
    1: string message;
}

exception ThriftInvalidUserException {
    1: string message;
}

exception ThriftNumberTakenException {
    1: string message;
}

exception ThriftRaceFullException {
    1: string message;
}

service ThriftRunficService{
    i64 addRace(1:string city, 2: string description, 3: string startDateTime, 4: double price, 5: i32 maxParticipants ) throws (1: ThriftInputValidationException e)

    ThriftRaceDto findRace(1: i64 raceID) throws (1: ThriftInstanceNotFoundException e)

    list<ThriftRaceDto> findByDate(1: string date, 2: string city)

    ThriftInscriptionDto inscribe(1: i64 raceID, 2: string email, 3: string creditCardNumber) throws (1: ThriftInputValidationException e, 2: ThriftInscriptionClosedException ee, 3: ThriftInstanceNotFoundException eee, 4: ThriftRaceFullException eeee, 5: ThriftAlreadyInscribedException eeeee)

    list<ThriftInscriptionDto> findAllFromUser(1: string email) throws (1: ThriftInputValidationException e)

    ThriftInscriptionDto getRunnerNumber(1: i64 inscriptionID, 2: string creditCardNumber) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee, 3: ThriftNumberTakenException eee, 4: ThriftInvalidUserException eeee)
}