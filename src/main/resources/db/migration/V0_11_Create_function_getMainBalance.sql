
CREATE OR REPLACE FUNCTION get_latest_balance(account_id VARCHAR)
    RETURNS DOUBLE PRECISION AS $$
DECLARE
    latest_balance DOUBLE PRECISION;
BEGIN
    SELECT balance.mainBalance INTO latest_balance
    FROM balance
             INNER JOIN account ON balance.idAccount = account.id
    WHERE balance.idAccount = account_id
    ORDER BY balance.date DESC
    LIMIT 1;

    RETURN latest_balance;
END;
$$ LANGUAGE plpgsql;