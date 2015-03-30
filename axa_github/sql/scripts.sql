CREATE TABLE IF NOT EXISTS Connexions (
	id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ip int unsigned NOT NULL,
	date_co timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Mots (
	id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	mot varchar(50) NOT NULL,
	id_connexion bigint NOT NULL,
	FOREIGN KEY (id_connexion) REFERENCES Connexions(id)
);

drop procedure if exists addMessage;
CREATE PROCEDURE addMessage (IN ip varchar(15),IN message varchar(500), OUT error varchar(50))
main:BEGIN
set @delim = ' ';
if ip is null then
	set error='L''adresse ip est null';
	leave main;
end if;
if message is null then
	set error='Le message est null';
	leave main;
end if;
set @ip = INET_ATON(ip);
INSERT INTO Connexions (ip) values(@ip);
set @id_connexion = LAST_INSERT_ID();
set @nbMots = LENGTH(message) - LENGTH(REPLACE(message, @delim, ''))+1;

if @nbMots = 1 then
	INSERT INTO Mots (mot,id_connexion) values(lower(message),@id_connexion);
	leave main;
end if;
set @nbIter = 1;

WHILE @nbIter<=@nbMots DO
	set @mot = SUBSTRING_INDEX(SUBSTRING_INDEX(message,@delim,-(@nbMots-@nbIter+1)),@delim,1);
    INSERT INTO Mots (mot,id_connexion) values(lower(@mot),@id_connexion);
	set @nbIter=@nbIter+1;
END WHILE;
leave main;
END;

call addMessage('127.0.0.1',null,@error);
select @error;
call addMessage(null,'test',@error2);
select @error2;
call addMessage('127.0.0.1','test',@error3);
call addMessage('127.0.0.1','test lol bible jean',@error4);

drop procedure if exists topIp;
CREATE PROCEDURE topIp (IN limite int)
BEGIN
set @limite=limite;
set @requete='select INET_NTOA(ip) as IP, count(*) as nbConnection from Connexions group by INET_NTOA(ip) order by nbConnection desc limit ?';
    PREPARE stmt FROM @requete;
    EXECUTE stmt USING @limite;
    DEALLOCATE PREPARE stmt;
END;

call topIp(5);

drop procedure if exists topMotsCles;
CREATE PROCEDURE topMotsCles (IN limite int)
BEGIN
set @limite=limite;

set @requete='select mot, count(*) as nbRecu from Mots group by mot Order by nbRecu DESC limit ?';
    PREPARE stmt FROM @requete;
    EXECUTE stmt USING @limite;
    DEALLOCATE PREPARE stmt;
END;

call topMotsCles(2);

drop procedure if exists topMotsClesOrderBy;
CREATE PROCEDURE topMotsClesOrderBy (IN limite int,IN orderby int(1))
BEGIN
set @limite=limite;
if orderby=2 then
set @orderby = 'DESC';
else
set @orderby = 'ASC';
end if;
set @requete=CONCAT('select m.mot, nbRecu from Mots as m INNER JOIN (select id,count(*) as nbRecu from Mots group by mot Order by nbRecu DESC LIMIT ?) as m2 on m2.id=m.id Order by m.mot ',@orderby);
    PREPARE stmt FROM @requete;
    EXECUTE stmt USING @limite;
    DEALLOCATE PREPARE stmt;
END;

call topMotsClesOrderBy(2,1);