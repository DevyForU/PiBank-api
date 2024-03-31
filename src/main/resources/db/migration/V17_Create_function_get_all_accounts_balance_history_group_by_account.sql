CREATE OR REPLACE FUNCTION get_all_accounts_balance_history_group_by_account()
RETURNS TABLE (
    id VARCHAR,
    id_account VARCHAR,
    main_balance DOUBLE PRECISION,
    loans DOUBLE PRECISION,
    loans_interest DOUBLE PRECISION,
    date TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY 
    SELECT
        id,
        id_account,
        main_balance,
        loans,
        loans_interest,
        date
    FROM
        balance_history
    ORDER BY
        id_account, date DESC;
END;
$$ LANGUAGE plpgsql;
