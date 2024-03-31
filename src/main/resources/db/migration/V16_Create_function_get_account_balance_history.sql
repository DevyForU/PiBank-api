CREATE OR REPLACE FUNCTION get_account_balance_history(account_id_param VARCHAR)
RETURNS TABLE (
    id VARCHAR,
    main_balance DOUBLE PRECISION,
    loans DOUBLE PRECISION,
    loans_interest DOUBLE PRECISION,
    date TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY 
    SELECT
        id,
        main_balance,
        loans,
        loans_interest,
        date
    FROM
        balance_history
    WHERE
        id_account = account_id_param
    ORDER BY
        date DESC;
END;
$$ LANGUAGE plpgsql;
