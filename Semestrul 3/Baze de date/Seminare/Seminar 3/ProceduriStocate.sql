USE ParcDistractii;

GO
CREATE PROCEDURE AdaugaSectiune @nume varchar(100),@descriere varchar(100)
AS
BEGIN
INSERT INTO Sectiune(nume,descriere)
VALUES (@nume,@descriere)
END;

GO
EXEC AdaugaSectiune 'sectiunee','descrieree'

GO
CREATE PROCEDURE ActualizeazaEmail @idV INT,@emailNou VARCHAR(100)
AS
BEGIN
UPDATE Vizitator
SET email=@emailNou
WHERE idV=@idV
END;

EXEC ActualizeazaEmail 5,'emailNou'
EXEC ActualizeazaEmail 1,'emailLiviu'

GO
CREATE PROCEDURE AfiseazaVizitatoriCareAuDatNote
AS
BEGIN
SELECT v.nume,v.email,COUNT(va.nota) as NrNote
FROM Vizitator v
RIGHT JOIN VizitatorAtractie va ON v.idV=va.idV
GROUP BY v.nume,v.email
END;

EXEC AfiseazaVizitatoriCareAuDatNote

GO
CREATE PROCEDURE InsereazaCategorie @nume VARCHAR(100)
AS
BEGIN
IF ((SELECT COUNT(nume) FROM CategorieVizitatori WHERE nume=@nume)>0)
	RAISERROR('Exista deja o categorie cu acest nume!',16,1);
ELSE
	INSERT INTO CategorieVizitatori(nume)
	VALUES (@nume);
END;

EXEC InsereazaCategorie 'categorieNoua'
EXEC InsereazaCategorie 'categorieee'

SELECT * FROM Sectiune
SELECT * FROM Vizitator
SELECT * FROM VizitatorAtractie
SELECT * FROM CategorieVizitatori

