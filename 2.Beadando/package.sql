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
