class CustomError(Exception):
    pass

class RepositoryError(CustomError):
    pass

class ValidationError(CustomError):
    pass