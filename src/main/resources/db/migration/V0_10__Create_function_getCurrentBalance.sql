CREATE OR REPLACE FUNCTION getLatestBalance(accountId VARCHAR)
RETURNS TABLE (
    id VARCHAR,
    idAccount VARCHAR,
    mainBalance DOUBLE PRECISION,
    loans DOUBLE PRECISION,
    loansInterest DOUBLE PRECISION,
    date TIMESTAMP WITHOUT TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT id, idAccount, mainBalance, loans, loansInterest, date
    FROM balance
    WHERE idAccount = account_id
    ORDER BY date DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;
