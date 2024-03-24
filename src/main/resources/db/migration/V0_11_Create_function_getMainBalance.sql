 
CREATE OR REPLACE FUNCTION getMainBalance(accountId varchar)
RETURNS double precision AS
$$
DECLARE
    mainBalance double precision;
BEGIN
    SELECT mainBalance INTO main_balance
    FROM "balance"
    WHERE idAccount = accountId;

    RETURN mainBalance;
END;
$$
LANGUAGE plpgsql;
