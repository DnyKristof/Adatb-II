CREATE OR REPLACE PROCEDURE RANDOM_AUTO(num INT) is
 betuk VARCHAR2(6);
  szamok VARCHAR2(6);
  rendszam VARCHAR2(13);
BEGIN
    FOR I IN 1..num LOOP
        SELECT DBMS_RANDOM.STRING('U', 3) INTO betuk FROM DUAL;
        SELECT LPAD(TRUNC(DBMS_RANDOM.VALUE(0, 1000)), 3, '0') INTO szamok FROM DUAL;
        rendszam := betuk || '-' || szamok;
        INSERT INTO AUTO VALUES(I,rendszam);
    END LOOP;
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;


CREATE OR REPLACE PROCEDURE FILEBOL_ORVOS AS
FAJL UTL_FILE.FILE_TYPE;
LINE VARCHAR(200);
COL INT;
EMBER ORVOS%ROWTYPE;
ID INT;
BEGIN
    FAJL:=UTL_FILE.FOPEN('TESZT','orvosok.txt','R');
    LOOP
        COL:=1;
        UTL_FILE.GET_LINE(FAJL,LINE);
        
        --Szetkapkodjuk a sort vesszőnként ( [^,] minden karakter ami nem vessző , + ---> álljon meg egyből az első vesszőnél
            FOR I IN (SELECT REGEXP_SUBSTR (LINE, '[^,]+', 1, level) AS STR FROM DUAL CONNECT BY REGEXP_SUBSTR(LINE, '[^,]+', 1, level) IS NOT NULL) LOOP
            IF COL = 0 THEN
                EMBER.Nev := I.STR;
            ELSIF COL = 1 THEN
                EMBER.Fizetes := TO_NUMBER(I.STR);
            END IF;
            COL:= COL+1;
            SELECT FLOOR(DBMS_RANDOM.VALUE(1, 1001)) INTO ID FROM DUAL;
            INSERT INTO ORVOS VALUES(ID,EMBER.Nev,EMBER.Fizetes);
            
            END LOOP;
            
        END LOOP;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        UTL_FILE.FCLOSE(FAJL);
    WHEN OTHERS THEN
        ROLLBACK;
END;



CREATE OR REPLACE PROCEDURE UJ_HIVAS(Nev VARCHAR2,Telefonszam VARCHAR2,Hely VARCHAR2,Diszpecser VARCHAR2,Kikuldve INT) IS
ID INT;
DATUM DATE;
BEGIN
    SELECT COUNT(*) INTO ID FROM BEJELENTES;
    
    IF ID=0 THEN
        ID:=1;
    ELSE 
        SELECT MAX(BID)+1 INTO ID FROM BEJELENTES;
    END IF;
    SELECT SYSDATE INTO DATUM FROM DUAL;
    
    INSERT INTO BEJELENTES VALUES(ID,DATUM,Nev,Telefonszam,Hely,Diszpecser,Kikuldve);
    
    COMMIT;
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        
END;

CREATE OR REPLACE FUNCTION NAPI_HIVASSZAM(PDATUM DATE) RETURN INTEGER IS
SZAM INT;
BEGIN
    SELECT COUNT(*) INTO SZAM FROM BEJELENTES WHERE BEJELENTES.DATUM = PDATUM;
    
    RETURN SZAM;
END;





create or replace PROCEDURE KIVONULAS_FELTOLTES(szam INT) IS
CSID INT;
AID INT;
BID INT;
KID INT :=1;

TYPE Leirasok IS TABLE OF VARCHAR2(100);

LEIR Leirasok := Leirasok('Autóbaleset','Megbotlás','Támadás','Utcai harc','Háztartásibeli baleset');

Random_index NUMBER;
Random_Leiras Varchar2(100);

Random_Indulas TIMESTAMP;
Random_Erkezes TIMESTAMP;


BEGIN

    DELETE FROM KIVONULAS;

    FOR I IN 1..szam LOOP

        Random_index := DBMS_RANDOM.VALUE(1,LEIR.COUNT);
        Random_Leiras := LEIR(Random_index);

        SELECT SYSDATE - DBMS_RANDOM.VALUE(0, 50) INTO Random_Indulas FROM DUAL;
        SELECT SYSDATE - DBMS_RANDOM.VALUE(60, 110) INTO Random_Erkezes FROM DUAL;

        SELECT CSID INTO CSID FROM (SELECT CSID FROM CSAPAT ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM = 1;
        SELECT AID INTO AID FROM (SELECT AID FROM AUTO ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM = 1;
        SELECT BID INTO BID FROM (SELECT BID FROM BEJELENTES ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM = 1;

        INSERT INTO KIVONULAS VALUES(KID,Random_Indulas,Random_Erkezes,Random_Leiras,CSID,AID,BID);

        KID:=KID+1;
        COMMIT;
    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;

END;




CREATE OR REPLACE PROCEDURE HIVAS_ADMINISZTRALASA(PBID INT,Kimente NUMBER) IS
BEGIN
UPDATE BEJELENTES SET Kikuldve=Kimente WHERE BID=PBID;
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;



CREATE OR REPLACE PROCEDURE HIVAS_NEVRE(PNEV VARCHAR2,CURSOR OUT SYS_REFCURSOR) IS
BEGIN
    OPEN CURSOR FOR SELECT * FROM BEJELENTES WHERE Hivo=PNEV;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('HIBA TÖRTÉNT');
END;





CREATE OR REPLACE PROCEDURE HIVAS_IDOSZAK(MIN DATE,MAX DATE,CURSOR OUT SYS_REFCURSOR) IS
BEGIN
    OPEN CURSOR FOR SELECT * FROM BEJELENTES WHERE Datum >= MIN and Datum<= MAX;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('HIBA TÖRTÉNT');
END;



create or replace PACKAGE MENTO AS
    PROCEDURE RANDOM_AUTO(num INT);
    PROCEDURE FILEBOL_ORVOS;
    PROCEDURE UJ_HIVAS(Nev VARCHAR2,Telefonszam VARCHAR2,Hely VARCHAR2,Diszpecser VARCHAR2,Kikuldve INT);
    FUNCTION NAPI_HIVASSZAM(PDATUM DATE) RETURN INTEGER;
    PROCEDURE KIVONULAS_FELTOLTES(szam INT);
    PROCEDURE HIVAS_ADMINISZTRALASA(PBID INT, Kimente NUMBER);
    PROCEDURE HIVAS_NEVRE(PNEV VARCHAR2,CURSOR OUT SYS_REFCURSOR);
    PROCEDURE HIVAS_IDOSZAK(MIN DATE,MAX DATE,CURSOR OUT SYS_REFCURSOR);
END;






