CREATE OR REPLACE FUNCTION get_account_details(account_number_param TEXT) 
RETURNS TABLE (
    account_number TEXT,
    main_balance NUMERIC,
    loans NUMERIC,
    loans_interest NUMERIC,
    date TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY 
    SELECT
        "account".account_number,
        "balance_history".main_balance,
        "account".loans,
        "account".loans_interest,
        "balance_history".date
    FROM
        "balance_history"
    JOIN
        "account" ON balance_history.id_account = "account".id
    JOIN
        "bank" ON account.id_bank = "bank".id
    JOIN
        "user" ON "account".id_user = "user".id
    WHERE
        "account".account_number = account_number_param;
END;
$$ LANGUAGE plpgsql;
