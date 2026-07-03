class SongManagementException(Exception):
    pass


class RepositoryException(SongManagementException):
    def __init__(self, msg):
        self.__msg = msg

    def get_message(self):
        return self.__msg

    def __str__(self):
        return "Repository exception:" + str(self.__msg)


class PersonAlreadyExistsException(RepositoryException):
    def __init__(self):
        RepositoryException.__init__(self, "Persoana exista deja.")


class PersonDoesNotExistException(RepositoryException):
    def __init__(self):
        RepositoryException.__init__(self, "Nu exista persoana.")


class SongAlreadyExistsException(RepositoryException):
    def __init__(self):
        RepositoryException.__init__(self, "Meloadia exista deja.")


class SongDoesNotExistException(RepositoryException):
    def __init__(self):
        RepositoryException.__init__(self, "Nu exista melodia..")


class RatingAlreadyExistsException(RepositoryException):
    def __init__(self):
        RepositoryException.__init__(self, "O evaluare pentru aceasta persoana si pentru aceasta melodie exista deja.")


class ValidationException(SongManagementException):
    def __init__(self, msg):
        self.__msg = msg

    def __str__(self):
        return "Validation exception:" + str(self.__msg)
