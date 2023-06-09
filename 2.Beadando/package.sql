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
